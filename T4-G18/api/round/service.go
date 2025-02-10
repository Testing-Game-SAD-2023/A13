package round

import (
	"errors"

	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{
		db: db,
	}
}

func (rs *Repository) Create(r *CreateRequest) (Round, error) {
	var round model.Round

	err := rs.db.Transaction(func(tx *gorm.DB) error {

		var lastRound model.Round
		err := tx.Where(&model.Round{GameID: r.GameId}).
			Order("\"order\" desc").
			Last(&lastRound).
			Error

		if err != nil && !errors.Is(err, gorm.ErrRecordNotFound) {
			return err
		}

		round = model.Round{
			GameID:      r.GameId,
			TestClassId: r.TestClassId,
			StartedAt:   r.StartedAt,
			ClosedAt:    r.ClosedAt,
			Order:       lastRound.Order + 1,
		}

		return tx.Create(&round).Error

	})

	return fromModel(&round), api.MakeServiceError(err)
}

func (rs *Repository) Update(id int64, r *UpdateRequest) (Round, error) {

	var (
		round model.Round = model.Round{ID: id}
		err   error
	)

	err = rs.db.Model(&round).Updates(r).Error
	if err != nil {
		return Round{}, api.MakeServiceError(err)
	}

	return fromModel(&round), api.MakeServiceError(err)
}

func (rs *Repository) FindById(id int64) (Round, error) {
	var round model.Round

	err := rs.db.
		First(&round, id).
		Error

	return fromModel(&round), api.MakeServiceError(err)
}

func (rs *Repository) FindByGame(id int64) ([]Round, error) {
	var rounds []model.Round

	err := rs.db.
		Where(&model.Round{GameID: id}).
		Order("\"order\" asc").
		Find(&rounds).
		Error

	resp := make([]Round, len(rounds))
	for i, round := range rounds {
		resp[i] = fromModel(&round)
	}

	return resp, api.MakeServiceError(err)
}

func (rs *Repository) Delete(id int64) error {
	err := rs.db.Transaction(func(tx *gorm.DB) error {
		var round model.Round
		db := rs.db.
			Where(&model.Round{ID: id}).
			Clauses(clause.Returning{}).
			Delete(&round)

		if db.Error != nil {
			return db.Error
		} else if db.RowsAffected < 1 {
			return api.ErrNotFound
		}

		err := rs.db.
			Model(&model.Round{}).
			Where(&model.Round{GameID: round.GameID}).
			Where("\"order\" > ?", round.Order).
			UpdateColumn("order", gorm.Expr("\"order\" - ?", 1)).
			Error

		return err
	})

	return api.MakeServiceError(err)
}
