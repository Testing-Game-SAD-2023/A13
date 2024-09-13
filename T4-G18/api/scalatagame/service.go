package scalatagame

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

func (gs *Repository) Create(r *CreateRequest) (ScalataGame, error) {
	var (
		scalatagame = model.ScalataGame{
			ScalataName: r.ScalataName,
			PlayerID:    r.PlayerID,
			CreatedAt:   r.CreatedAt,
		}
	)

	err := gs.db.Transaction(func(tx *gorm.DB) error {
		return tx.Create(&scalatagame).Error
	})

	if err != nil {
		return ScalataGame{}, api.MakeServiceError(err)
	}

	return fromModel(&scalatagame), nil
}

func (gs *Repository) FindById(id int64) (ScalataGame, error) {
	var scalataGame model.ScalataGame
	// err := gs.db.
	// 	//Preload("Players").
	// 	First(&scalataGame, id).
	// 	Error
	err := gs.db.
		Preload("Games").
		First(&scalataGame, id).
		Error
	return fromModel(&scalataGame), api.MakeServiceError(err)
}

func (gs *Repository) FindByInterval(accountId string, i api.IntervalParams, p api.PaginationParams) ([]ScalataGame, int64, error) {
	var (
		scalataGames []model.ScalataGame
		n            int64
		err          error
	)

	// TODO: check if accountId is used in this case
	// if accountId != "" {
	// 	err = gs.db.Transaction(func(tx *gorm.DB) error {
	// 		association := tx.Model(&model.Player{AccountID: accountId}).
	// 			Scopes(api.WithInterval(i, "games.created_at"),
	// 				api.WithPagination(p)).
	// 			Order("games.created_at desc").
	// 			Association("Games")

	// 		n = association.Count()
	// 		return association.Find(&scalataGames)

	// 	})
	// } else {
	err = gs.db.Preload("Games").Scopes(api.WithInterval(i, "scalata_games.created_at"),
		api.WithPagination(p)).
		Find(&scalataGames).
		Count(&n).
		Error
	// }
	res := make([]ScalataGame, len(scalataGames))
	for i, scalata := range scalataGames {
		res[i] = fromModel(&scalata)
	}
	return res, n, api.MakeServiceError(err)
}

func (gs *Repository) Delete(id int64) error {
	db := gs.db.
		Where(&model.ScalataGame{ID: id}).
		Delete(&model.ScalataGame{})

	if db.Error != nil {
		return api.MakeServiceError(db.Error)
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}
	return nil
}

func (gs *Repository) Update(id int64, r *UpdateRequest) (ScalataGame, error) {

	var (
		scalataGame model.ScalataGame = model.ScalataGame{ID: id}
		err         error
	)

	err = gs.db.Model(&scalataGame).Updates(r).Error
	if err != nil {
		return ScalataGame{}, api.MakeServiceError(err)
	}

	return fromModel(&scalataGame), api.MakeServiceError(err)
}
