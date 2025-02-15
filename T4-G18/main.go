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

	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/api/game"
	"github.com/alarmfox/game-repository/api/robot"
	"github.com/alarmfox/game-repository/api/round"
	"github.com/alarmfox/game-repository/api/scalatagame"
	"github.com/alarmfox/game-repository/api/playerhascategoryachievement"
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

/*
*	il punto di ingresso del programma
*/
func main() {

	/*
	*	Definisce un flag -config per specificare il percorso del file di configurazione (di default config.json).
	*	flag.Parse() analizza i flag passati alla riga di comando.
	*	ctx è un context di base, usato per gestire il ciclo di vita dell'applicazione
	*
	*/
	var (
		configPath = flag.String("config", "config.json", "Path for configuration")
		ctx        = context.Background()
	)
	flag.Parse()

	/*
	*	Se il file non esiste o ha problemi, lancia un errore e termina l’applicazione (log.Fatal).
	*/
	fcontent, err := os.ReadFile(*configPath)
	if err != nil {
		log.Fatal(err)
	}

	/*
	*	Deserializza il JSON in una struttura Configuration usando json.Unmarshal.
	*	Se mancano chiavi obbligatorie, json.Unmarshal fallisce.
	*/
	var configuration Configuration
	if err := json.Unmarshal(fcontent, &configuration); err != nil {
		log.Fatal(err)
	}

	// Applica valori predefiniti alla configurazione nel caso in cui alcuni campi siano mancanti.
	makeDefaults(&configuration)

	/*
	*	Intercetta segnali di terminazione (SIGTERM, os.Interrupt), il contesto ctx viene annullato.
	*/
	ctx, canc := signal.NotifyContext(ctx, syscall.SIGTERM, os.Interrupt)
	defer canc()

	/*
	*	Passo all'avvio del servzio con la funzione run 
	*/
	if err := run(ctx, configuration); err != nil {
		log.Fatal(err)
	}
}

func run(ctx context.Context, c Configuration) error {

	/*
	*	Apre una connessione a un database PostgreSQL utilizzando GORM.
	*	SkipDefaultTransaction: true: Disabilita le transazioni automatiche per migliorare le performance.
	*   TranslateError: true: Converte gli errori SQL in errori GORM leggibili
	*/
	db, err := gorm.Open(postgres.Open(c.PostgresUrl), &gorm.Config{
		SkipDefaultTransaction: true,
		TranslateError:         true,
	})

	if err != nil {
		return err
	}

	/*
	*	AutoMigrate crea o aggiorna 
	*   automaticamente le tabelle nel database in base alle strutture definite nei modelli Go.
	*/
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
	)

	if err != nil {
		return err
	}

	/*
	*  Questo definisce una relazione molti-a-molti tra Game e Player attraverso la tabella PlayerGame.
	*/
	if err := db.SetupJoinTable(&model.Game{}, "Players", &model.PlayerGame{}); err != nil {
		return err
	}

	/*
	*  Crea una directory per i file di dati necessari al servizio.
	*/
	if err := os.Mkdir(c.DataDir, os.ModePerm); err != nil && !errors.Is(err, os.ErrExist) {
		return fmt.Errorf("cannot create data directory: %w", err)
	}

	/*
	*	inizializza un router HTTP per gestire le API REST.
	*/
	r := chi.NewRouter()

	/*
	*	Cross-Origin Resource Sharing
	*   Configura CORS, consentendo a qualsiasi origine di fare richieste HTTP.
	*/
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Content-Type", "Accept", "Authorization"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	/*
	*	SwaggerUI serve la documentazione delle API a /docs
	*/
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

	/*
	*   Endpoint metriche Prometheus
	*/
	r.Handle("/metrics", promhttp.Handler())


	/*
	*	middleware.RealIP: Ottiene l'IP reale del client.
	*	middleware.Logger: Registra le richieste HTTP.
	*   middleware.Recoverer: Evita crash gestendo panics.
	*
	*/
	clientLimiter := limiter.NewClientLimiter(c.RateLimiting.Burst, c.RateLimiting.MaxRate)
	r.Group(func(r chi.Router) {

		r.Use(middleware.RealIP)
		r.Use(middleware.Logger)
		r.Use(middleware.Recoverer)

		if c.RateLimiting.Enabled {
			r.Use(clientLimiter.Limit)
		}

		/*
		*	Attiva autenticazione JWT 
		*/
		if c.Authentication.Enabled {
			r.Use(api.WithJWTAuthentication(api.JWTAuthenticationConfig{
				HeaderKey:    c.Authentication.HeaderKey,
				Method:       c.Authentication.Method,
				AuthEndpoint: c.Authentication.AuthEndpoint,
			}))
		}

		/*
		*	Crea i controller delle API, che gestiscono la logica di business 
		*/
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
		)

		/* Registra gli endpoint API.
		*/
		r.Mount(c.ApiPrefix, setupRoutes(
			gameController,
			roundController,
			turnController,
			robotController,
			scalataController,
			phcaController,
		))
	})

	/*
	*   errgroup.WithContext(ctx): Crea un gruppo di goroutine per gestire attività concorrenti.
	*	g.GO Avvio del Server 
	*/
	log.Printf("listening on %s", c.ListenAddress)
	g, ctx := errgroup.WithContext(ctx)

	g.Go(func() error {
		return startHttpServer(ctx, r, c.ListenAddress)
	})


	/*
	*	Ogni c.CleanupInterval, esegue una pulizia del database.
	*/
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

	/*
	*	Se il rate limiting è attivo, pulisce periodicamente gli utenti bloccati.
	*/
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

	// attende la terminazione di tutte le goroutine.
	return g.Wait()
}

func startHttpServer(ctx context.Context, r chi.Router, addr string) error {
	/*
	*	Configurazione del server 
	*/
	server := http.Server{
		Addr:              addr, 	//  l'indirizzo a cui il server deve ascoltare 
		Handler:           r,		//  il router HTTP (r), che gestisce le richieste
		ReadTimeout:       time.Minute,
		WriteTimeout:      time.Minute,
		IdleTimeout:       time.Minute,
		ReadHeaderTimeout: 10 * time.Second,
		MaxHeaderBytes:    1024 * 8,
	}

	/*
	*	Gestione del server in un goroutine separato:
	*	Un canale errCh viene creato per ricevere eventuali errori 
	*	che potrebbero verificarsi durante l'esecuzione del server.
	*   Un goroutine separato viene avviato per eseguire server.ListenAndServe(), 
	*	che avvia il server e inizia a gestire le richieste HTTP.
	*   Se il server restituisce un errore diverso da http.ErrServerClosed 
	*	(che indica che il server è stato fermato correttamente), l'errore viene inviato al canale errCh.
	*/
	errCh := make(chan error)
	defer close(errCh)
	go func() {
		if err := server.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			errCh <- err
		}
	}()

	/*
	*	il select attende su due canali:
	*	ctx.Done(): Se il contesto viene annullato 
	*   (per esempio, il programma riceve un segnale di interruzione o di terminazione), 
	*	il server deve essere fermato.
	*   errCh: Se il server restituisce un errore (diverso da una chiusura regolare), 
	*	la funzione restituisce quell'errore.
	*   Se uno di questi canali viene attivato, il select interrompe l'attesa e il controllo passa alla parte successiva.
	*/
	select {
		case <-ctx.Done():
		case err := <-errCh:
			return err
	}

	/*
	*	Viene creato un nuovo contesto ctx con un timeout di 10 secondi per garantire 
	*   che il server venga arrestato entro un tempo ragionevole.
	*   server.Shutdown(ctx) viene chiamato per fermare il server,
	*   attendendo il completamento delle operazioni in corso e garantendo
	*   che tutte le connessioni vengano chiuse correttamente.
	*	Se l'arresto non avviene entro 10 secondi, il contesto verrà annullato 
	*	e il server sarà terminato forzatamente.
	*/
	ctx, canc := context.WithTimeout(context.Background(), time.Second*10)
	defer canc()
	return server.Shutdown(ctx)
}

/*
*	La funzione cleanup gestisce un'operazione di pulizia nel database e nel filesystem
*/
func cleanup(db *gorm.DB) (int64, error) {

	/*
	*	metadata: una slice che contiene i record della tabella Metadata, estratti dal database.
	*	err: una variabile che memorizza eventuali errori che si verificano durante l'esecuzione.
	*   n: una variabile che memorizza il numero di record trovati nel database.
	*/
	var (
		metadata []model.Metadata
		err      error
		n        int64
	)

	/*
	*	Viene avviata una transazione tramite db.Transaction, 
	*	che garantisce che tutte le operazioni eseguite all'interno di questa transazione
	* 	vengano eseguite come un'unica unità atomica. Se una delle operazioni fallisce, 
	*	tutte le modifiche al database vengono annullate
	*
	*/
	err = db.Transaction(func(tx *gorm.DB) error {

		/*
		*	seleziona tutti i record dalla tabella Metadata che hanno un campo turn_id NULL.
		*	Find(&metadata): esegue la query e memorizza i risultati nella variabile metadata.
		*	Count(&n): conta il numero di record trovati e memorizza il conteggio in n.
		*/
		err := tx.
			Where("turn_id IS NULL").
			Find(&metadata).
			Count(&n).
			Error

		if err != nil {
			return err
		}

		/*
		*	Viene creato un array deleted che terrà traccia degli ID dei record che sono stati cancellati.
		*   Per ogni record in metadata, viene tentata l'eliminazione del file associato tramite os.Remove(m.Path), 
		*	dove m.Path è il percorso del file nel filesystem.
		*	Se il file non esiste (os.ErrNotExist), l'errore non viene registrato. 
		*	Se c'è un altro tipo di errore durante la rimozione del file, viene stampato nel log.
		*	Se l'eliminazione del file ha successo, l'ID del record viene aggiunto alla lista deleted.
		*/
		var deleted []int64
		for _, m := range metadata {
			if err := os.Remove(m.Path); err != nil && !errors.Is(err, os.ErrNotExist) {
				log.Print(err)
			} else {
				deleted = append(deleted, m.ID)
			}
		}
		/*
		*	Una volta che i file sono stati eliminati, 
		*	la transazione elimina i record dalla tabella Metadata che hanno gli ID presenti nella lista deleted.
		*/
		return tx.Delete(&[]model.Metadata{}, deleted).Error
	})

	return n, err
}

func makeDefaults(c *Configuration) {
	/*
	*	Controllo e impostazione del prefisso API
	*   per definire il prefisso per le rotte
	*/
	if c.ApiPrefix == "" {
		c.ApiPrefix = "/"
	}

	/*
	*	Questo definisce l'indirizzo su cui il server HTTP deve essere in ascolto. 
	*	Il valore predefinito è utile durante lo sviluppo.
	*/
	if c.ListenAddress == "" {
		c.ListenAddress = "localhost:3000"
	}

	/*
	*	La directory in cui vengono memorizzati i dati dell'applicazione.
	*	Se non specificato, viene utilizzata la cartella data di default.
	*/
	if c.DataDir == "" {
		c.DataDir = "data"
	}

	/*
	*	Il campo CleanupInterval indica la frequenza con cui il sistema dovrebbe eseguire 
	*	attività di pulizia (come la rimozione di file obsoleti o la gestione delle risorse). 
	*	Se non specificato, il valore predefinito è un'ora.
	*/
	if int64(c.CleanupInterval) == 0 {
		c.CleanupInterval = time.Hour
	}
}

/*	
*	La funzione setupRoutes definisce le rotte per un'applicazione basata su chi router. 
*	In particolare, crea endpoint per varie entità 
*	Ogni entità ha un set di operazioni CRUD (Create, Read, Update, Delete) definite tramite rotte HTTP
*/
func setupRoutes(
				 gc *game.Controller, 
				 rc *round.Controller, 
				 tc *turn.Controller, 
				 roc *robot.Controller, 
				 sgc *scalatagame.Controller, 
				 pc *playerhascategoryachievement.Controller
				) *chi.Mux {

	r := chi.NewRouter()

	/*
	*	Viene applicato un middleware per limitare la dimensione massima del corpo della richiesta, 
	*	utilizzando una configurazione predefinita (DefaultBodySize). 
	*	Questo middleware è applicato a tutte le rotte, proteggendo l'app da richieste troppo grandi.
	*/
	r.Use(api.WithMaximumBodySize(api.DefaultBodySize))

	/*
	*	La funzione definisce un gruppo di rotte per ciascuna entità. 
	*	Ogni gruppo di rotte ha un set di operazioni come GET, POST, PUT, e DELETE per interagire con i dati associati. 
	*	Ogni rotta è associata a un handler che viene definito come una funzione.
	*/
	r.Route("/games", func(r chi.Router) {
		// Get game by his id
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

	return r
}
