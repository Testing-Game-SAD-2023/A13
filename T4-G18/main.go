package main

import (
	"context"
	"embed"
	"encoding/json"
	"errors"
	"flag"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
	"math/rand"
	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/api/game"
	"github.com/alarmfox/game-repository/api/leaderboard"
	"github.com/alarmfox/game-repository/api/playerhascategoryachievement"
	"github.com/alarmfox/game-repository/api/robot"
	"github.com/alarmfox/game-repository/api/round"
	"github.com/alarmfox/game-repository/api/scalatagame"
	"github.com/alarmfox/game-repository/api/turn"
	"github.com/alarmfox/game-repository/limiter"
	"github.com/alarmfox/game-repository/model"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	mw "github.com/go-openapi/runtime/middleware"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"golang.org/x/sync/errgroup"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Configuration struct {
	PostgresUrl     string        `json:"postgresUrl"`
	ListenAddress   string        `json:"listenAddress"`
	ApiPrefix       string        `json:"apiPrefix"`
	DataDir         string        `json:"dataDir"`
	EnableSwagger   bool          `json:"enableSwagger"`
	CleanupInterval time.Duration `json:"cleanupInterval"`
	RateLimiting    struct {
		Burst   int     `json:"burst"`
		MaxRate float64 `json:"maxRate"`
		Enabled bool    `json:"enabled"`
	} `json:"rateLimiting"`
	Authentication struct {
		Enabled      bool   `json:"enabled"`
		HeaderKey    string `json:"headerKey"`
		AuthEndpoint string `json:"authEndpoint"`
		Method       string `json:"method"`
	}
}

//go:embed postman
var postmanDir embed.FS

func createTrigger(db *gorm.DB) error {
	triggerFunction := `
        CREATE OR REPLACE FUNCTION update_player_stats() RETURNS TRIGGER AS $$
		BEGIN
			IF EXISTS (SELECT 1 FROM player_stats WHERE player_id = NEW.player_id) THEN
				UPDATE player_stats
                SET sfida_played_games = sfida_played_games + 1,
					sfida_won_games = sfida_won_games + CASE WHEN NEW.is_winner THEN 1 ELSE 0 END
				WHERE player_id = NEW.player_id;
			ELSE
                INSERT INTO player_stats (player_id, sfida_played_games, sfida_won_games)
				VALUES (NEW.player_id, 1, CASE WHEN NEW.is_winner THEN 1 ELSE 0 END);
			END IF;
			RETURN NEW;
		END;
		$$ LANGUAGE plpgsql;`

	trigger := `
        CREATE OR REPLACE TRIGGER after_player_games_insert
		AFTER INSERT ON player_games
		FOR EACH ROW
		EXECUTE FUNCTION update_player_stats();	
	`

	if err := db.Exec(triggerFunction).Error; err != nil {
		return err
	}
	if err := db.Exec(trigger).Error; err != nil {
		return err
	}
	return nil
}

func main() {
	var (
		configPath = flag.String("config", "config.json", "Path for configuration")
		ctx        = context.Background()
	)
	flag.Parse()

	fcontent, err := os.ReadFile(*configPath)
	if err != nil {
		log.Fatal(err)
	}

	var configuration Configuration
	if err := json.Unmarshal(fcontent, &configuration); err != nil {
		log.Fatal(err)
	}

	makeDefaults(&configuration)

	ctx, canc := signal.NotifyContext(ctx, syscall.SIGTERM, os.Interrupt)
	defer canc()

	if err := run(ctx, configuration); err != nil {
		log.Fatal(err)
	}
}

func run(ctx context.Context, c Configuration) error {

	db, err := gorm.Open(postgres.Open(c.PostgresUrl), &gorm.Config{
		SkipDefaultTransaction: true,
		TranslateError:         true,
	})

	if err != nil {
		return err
	}

	err = db.AutoMigrate(
		&model.ScalataGame{},
		&model.Game{},
		&model.Round{},
		&model.Player{},
		&model.Turn{},
		&model.Metadata{},
		&model.PlayerGame{},
		&model.Robot{},
		&model.PlayerHasCategoryAchievement{},
		&model.PlayerStats{},
	)

	if err != nil {
		return err
	}
	if err := db.SetupJoinTable(&model.Game{}, "Players", &model.PlayerGame{}); err != nil {
		return err
	}

	if err = createTrigger(db); err != nil {
		return err
	}

	//Start TEST DB
	tm := time.Now()
	for i := 0; i < 100000; i++ {
		playerGame := model.PlayerGame{
			PlayerID:  fmt.Sprintf("%d", i%250),
			GameID:    rand.Int63(),
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
			IsWinner:  rand.Intn(2) == 1,
		}
		if err := db.Create(&playerGame).Error; err != nil {
			//log.Fatalf("Failed to insert: %v", err)
			log.Println("Errore Insert")
		}
	}
	log.Printf("took %v", time.Since(tm))
	//	End TEST DB

	if err := os.Mkdir(c.DataDir, os.ModePerm); err != nil && !errors.Is(err, os.ErrExist) {
		return fmt.Errorf("cannot create data directory: %w", err)
	}

	r := chi.NewRouter()

	// basic cors
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Content-Type", "Accept", "Authorization"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	if c.EnableSwagger {
		r.Group(func(r chi.Router) {
			opts := mw.SwaggerUIOpts{SpecURL: "/public/postman/schemas/index.yaml"}
			sh := mw.SwaggerUI(opts, nil)
			r.Handle("/docs", sh)
			r.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
				http.Redirect(w, r, "/docs", http.StatusMovedPermanently)
			})
		})
	}

	// serving Postman directory for documentation files
	fs := http.FileServer(http.FS(postmanDir))
	r.Mount("/public/", http.StripPrefix("/public/", fs))

	// metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	clientLimiter := limiter.NewClientLimiter(c.RateLimiting.Burst, c.RateLimiting.MaxRate)
	r.Group(func(r chi.Router) {
		r.Use(middleware.RealIP)
		r.Use(middleware.Logger)
		r.Use(middleware.Recoverer)

		if c.RateLimiting.Enabled {
			r.Use(clientLimiter.Limit)
		}

		if c.Authentication.Enabled {
			r.Use(api.WithJWTAuthentication(api.JWTAuthenticationConfig{
				HeaderKey:    c.Authentication.HeaderKey,
				Method:       c.Authentication.Method,
				AuthEndpoint: c.Authentication.AuthEndpoint,
			}))
		}
		var (

			// game endpoint
			gameController = game.NewController(game.NewRepository(db))

			// round endpoint
			roundController = round.NewController(round.NewRepository(db))

			// turn endpoint
			turnController = turn.NewController(turn.NewRepository(db, c.DataDir))

			// robot endpoint
			robotController = robot.NewController(robot.NewRobotStorage(db))

			// scalatagame endpoint
			scalataController = scalatagame.NewController(scalatagame.NewRepository(db))

			// phca endpoint
			phcaController = playerhascategoryachievement.NewController(playerhascategoryachievement.NewRepository(db))

			// leaderboard endpoint
			leaderboardController = leaderboard.NewController(leaderboard.NewRepository(db))
		)

		r.Mount(c.ApiPrefix, setupRoutes(
			gameController,
			roundController,
			turnController,
			robotController,
			scalataController,
			phcaController,
			leaderboardController,
		))
	})
	log.Printf("listening on %s", c.ListenAddress)
	g, ctx := errgroup.WithContext(ctx)

	g.Go(func() error {
		return startHttpServer(ctx, r, c.ListenAddress)
	})

	g.Go(func() error {
		for {
			select {
			case <-time.After(c.CleanupInterval):
				_, err := cleanup(db)
				if err != nil {
					log.Print(err)
				}
			case <-ctx.Done():
				return nil
			}
		}
	})

	if c.RateLimiting.Enabled {
		g.Go(func() error {
			for {
				select {
				case <-ctx.Done():
					return nil
				case <-time.After(time.Minute):
					clientLimiter.Cleanup(3 * time.Minute)
				}
			}
		})

	}

	return g.Wait()

}

func startHttpServer(ctx context.Context, r chi.Router, addr string) error {
	server := http.Server{
		Addr:              addr,
		Handler:           r,
		ReadTimeout:       time.Minute,
		WriteTimeout:      time.Minute,
		IdleTimeout:       time.Minute,
		ReadHeaderTimeout: 10 * time.Second,
		MaxHeaderBytes:    1024 * 8,
	}

	errCh := make(chan error)
	defer close(errCh)
	go func() {
		if err := server.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			errCh <- err
		}
	}()

	select {
	case <-ctx.Done():
	case err := <-errCh:
		return err
	}

	ctx, canc := context.WithTimeout(context.Background(), time.Second*10)
	defer canc()

	return server.Shutdown(ctx)
}

func cleanup(db *gorm.DB) (int64, error) {
	var (
		metadata []model.Metadata
		err      error
		n        int64
	)

	err = db.Transaction(func(tx *gorm.DB) error {
		err := tx.
			Where("turn_id IS NULL").
			Find(&metadata).
			Count(&n).
			Error

		if err != nil {
			return err
		}

		var deleted []int64
		for _, m := range metadata {
			if err := os.Remove(m.Path); err != nil && !errors.Is(err, os.ErrNotExist) {
				log.Print(err)
			} else {
				deleted = append(deleted, m.ID)
			}
		}

		return tx.Delete(&[]model.Metadata{}, deleted).Error
	})

	return n, err
}

func makeDefaults(c *Configuration) {
	if c.ApiPrefix == "" {
		c.ApiPrefix = "/"
	}

	if c.ListenAddress == "" {
		c.ListenAddress = "localhost:3000"
	}

	if c.DataDir == "" {
		c.DataDir = "data"
	}

	if int64(c.CleanupInterval) == 0 {
		c.CleanupInterval = time.Hour
	}

}

func setupRoutes(gc *game.Controller, rc *round.Controller, tc *turn.Controller, roc *robot.Controller, sgc *scalatagame.Controller, pc *playerhascategoryachievement.Controller, lb *leaderboard.Controller) *chi.Mux {
	r := chi.NewRouter()

	r.Use(api.WithMaximumBodySize(api.DefaultBodySize))

	r.Route("/games", func(r chi.Router) {
		// Get game
		r.Get("/{id}", api.HandlerFunc(gc.FindByID))

		// Get games by player id
		r.Get("/player/{pid}", api.HandlerFunc(gc.FindByPID))

		// List games
		r.Get("/", api.HandlerFunc(gc.List))

		// Create game
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(gc.Create))

		// Update game
		r.With(middleware.AllowContentType("application/json")).
			Put("/{id}", api.HandlerFunc(gc.Update))

		// Delete game
		r.Delete("/{id}", api.HandlerFunc(gc.Delete))

	})

	r.Route("/rounds", func(r chi.Router) {
		// Get round
		r.Get("/{id}", api.HandlerFunc(rc.FindByID))

		// List rounds
		r.Get("/", api.HandlerFunc(rc.List))

		// Create round
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(rc.Create))

		// Update round
		r.With(middleware.AllowContentType("application/json")).
			Put("/{id}", api.HandlerFunc(rc.Update))

		// Delete round
		r.Delete("/{id}", api.HandlerFunc(rc.Delete))

	})

	r.Route("/turns", func(r chi.Router) {
		// Get turn
		r.Get("/{id}", api.HandlerFunc(tc.FindByID))

		// List turn
		r.Get("/", api.HandlerFunc(tc.List))

		// Create turn
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(tc.Create))

		// Update turn
		r.With(middleware.AllowContentType("application/json")).
			Put("/{id}", api.HandlerFunc(tc.Update))

		// Delete turn
		r.Delete("/{id}", api.HandlerFunc(tc.Delete))

		// Get turn file
		r.Get("/{id}/files", api.HandlerFunc(tc.Download))

		// Upload turn file
		r.With(middleware.AllowContentType("application/zip"),
			api.WithMaximumBodySize(api.MaxUploadSize)).
			Put("/{id}/files", api.HandlerFunc(tc.Upload))
	})

	r.Route("/robots", func(r chi.Router) {
		// Get robot with filter
		r.Get("/", api.HandlerFunc(roc.FindByFilter))

		// Create robots in bulk
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(roc.CreateBulk))

		// Delete robots by class id
		r.Delete("/", api.HandlerFunc(roc.Delete))

	})

	r.Route("/scalates", func(r chi.Router) {
		//Get scalates
		r.Get("/{id}", api.HandlerFunc(sgc.FindByID)) // TODO:

		// List scalates
		r.Get("/", api.HandlerFunc(sgc.List)) // TODO:

		// Create scalates
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(sgc.Create))

		// Update scalates
		r.With(middleware.AllowContentType("application/json")).
			Put("/{id}", api.HandlerFunc(sgc.Update))

		// Delete game
		// r.Delete("/{id}", api.HandlerFunc(sgc.Delete)) // TODO:

	})

	r.Route("/phca", func(r chi.Router) {
		// List PHCAs
		r.Get("/", api.HandlerFunc(pc.FindAll))

		// Get PHCA by Player ID
		r.Get("/{pid}", api.HandlerFunc(pc.FindByPID))

		// Create PHCA
		r.With(middleware.AllowContentType("application/json")).
			Post("/", api.HandlerFunc(pc.Create))

		// Update achievement
		r.With(middleware.AllowContentType("application/json")).
			Put("/{pid}/{statistic}", api.HandlerFunc(pc.Update))

		// Delete achievement
		r.Delete("/{pid}/{statistic}", api.HandlerFunc(pc.Delete))
	})

	r.Route("/leaderboard", func(r chi.Router) {
		// Get global leaderboard positions (for a given gamemode/statistic pair) from startPos to endPos
		r.Get("/subInterval/{gamemode}/{statistic}", api.HandlerFunc(lb.FindByInterval))
	})

	return r
}
