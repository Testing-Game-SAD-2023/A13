package game

import (
	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"
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
	var (
		game = model.Game{
			Name:      r.Name,
			StartedAt: r.StartedAt,
			ClosedAt:  r.ClosedAt,
			Players:   make([]model.Player, len(r.Players)),
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
		err = gs.db.Scopes(api.WithInterval(i, "games.created_at"),
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

	err = gs.db.Model(&game).Updates(r).Error
	if err != nil {
		return Game{}, api.MakeServiceError(err)
	}

	return fromModel(&game), api.MakeServiceError(err)
}
