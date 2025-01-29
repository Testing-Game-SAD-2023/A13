package game

import (
	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"

	"strconv"
)

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{
		db: db,
	}
}

func (gs *Repository) Create(r *CreateRequest) (Game, error) {
	//fmt.Println("(service.go game) Request received ", r)
	var (
		game = model.Game{
			Name:          r.Name,
			Difficulty:    r.Difficulty,
			StartedAt:     r.StartedAt,
			ClosedAt:      r.ClosedAt,
			ScalataGameID: r.ScalataGameID,
			Players:       make([]model.Player, len(r.Players)),
			Description:   r.Description,
		}
	)
	// detect duplication in player
	if api.Duplicated(r.Players) {
		return Game{}, api.ErrInvalidParam
	}

	for i, player := range r.Players {
		game.Players[i] = model.Player{
			AccountID: player,
		}
	}

	err := gs.db.Transaction(func(tx *gorm.DB) error {
		return tx.Create(&game).Error
	})

	if err != nil {
		return Game{}, api.MakeServiceError(err)
	}
	game.Players = nil

	return fromModel(&game), nil
}

func (gs *Repository) FindById(id int64) (Game, error) {
	var game model.Game
	err := gs.db.
		Preload("Players").
		First(&game, id).
		Error

	return fromModel(&game), api.MakeServiceError(err)
}

func (gs *Repository) FindByPID(pid int64) ([]Game, error) {
	var games []model.Game

	err := gs.db.
		Preload("Players").
		Where("id IN (SELECT game_id FROM player_games WHERE player_id = ?)", strconv.FormatInt(pid, 10)).
		Find(&games).
		Error

    res := make([]Game, len(games))
    for i, game := range games {
        res[i] = fromModel(&game)
    }

	return res, api.MakeServiceError(err)
}

func (gs *Repository) FindByInterval(accountId string, i api.IntervalParams, p api.PaginationParams) ([]Game, int64, error) {
	var (
		games []model.Game
		n     int64
		err   error
	)

	if accountId != "" {
		err = gs.db.Transaction(func(tx *gorm.DB) error {
			association := tx.Model(&model.Player{AccountID: accountId}).
				Scopes(api.WithInterval(i, "games.created_at"),
					api.WithPagination(p)).
				Order("games.created_at desc").
				Association("Games")

			n = association.Count()
			return association.Find(&games)

		})
	} else {
		err = gs.db.Preload("Players").Scopes(api.WithInterval(i, "games.created_at"),
			api.WithPagination(p)).
			Find(&games).
			Count(&n).
			Error
	}
	res := make([]Game, len(games))
	for i, game := range games {
		res[i] = fromModel(&game)
	}
	return res, n, api.MakeServiceError(err)
}

func (gs *Repository) Delete(id int64) error {
	db := gs.db.
		Where(&model.Game{ID: id}).
		Delete(&model.Game{})

	if db.Error != nil {
		return api.MakeServiceError(db.Error)
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}
	return nil
}

func (gs *Repository) Update(id int64, r *UpdateRequest) (Game, error) {
    var (
        game model.Game = model.Game{ID: id}
        err  error
    )

    // Effettuare l'aggiornamento in una transazione
    err = gs.db.Transaction(func(tx *gorm.DB) error {
        // Aggiorna i dati del gioco
        if err := tx.Model(&game).Updates(r).Error; err != nil {
            return err
        }

        // Se IsWinner è presente, aggiorna il campo is_winner nella tabella PlayerGames
        if r.IsWinner {
            // Aggiorna i record nella tabella PlayerGame
            if err := tx.Model(&model.PlayerGame{}).
                Where("game_id = ?", id).
                Update("is_winner", r.IsWinner).Error; err != nil {
                return err
            }

            // Recupera tutti i playerID associati al game
            var playerIDs []int64
            if err := tx.Model(&model.PlayerGame{}).
                Where("game_id = ?", id).
                Pluck("player_id", &playerIDs).Error; err != nil {
                return err
            }

            // Aggiorna il campo `games_won` per ogni player
            for _, playerID := range playerIDs {
                if err := tx.Model(&model.Player{}).
                    Where("id = ?", playerID).
                    UpdateColumn("games_won", gorm.Expr("games_won + ?", 1)).Error; err != nil {
                    return err
                }
            }
        }

        return nil
    })

    if err != nil {
        return Game{}, api.MakeServiceError(err)
    }

    return fromModel(&game), nil
}
