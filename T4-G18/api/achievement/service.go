package achievement

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

func (gs *Repository) Create(r *CreateRequest) (Achievement, error) {
    var (
        achievement = model.Achievement{
            Name:               r.Name,
            Category:           r.Category,
            ProgressRequired:   r.ProgressRequired,
        }
    )

    err := gs.db.Transaction(func(tx *gorm.DB) error {
        return tx.Create(&achievement).Error
    })

	if err != nil {
		return Achievement{}, api.MakeServiceError(err)
	}

    return fromModel(&achievement), nil
}

func (gs *Repository) FindById(id int64) (Achievement, error) {
	var achievement model.Achievement

	err := gs.db.
		First(&achievement, id).
		Error

	return fromModel(&achievement), api.MakeServiceError(err)
}

func (gs *Repository) Delete(id int64) error {
	db := gs.db.
		Where(&model.Achievement{ID: id}).
		Delete(&model.Achievement{})

	if db.Error != nil {
		return api.MakeServiceError(db.Error)
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}
	return nil
}

func (gs *Repository) Update(id int64, r *UpdateRequest) (Achievement, error) {

	var (
		achievement model.Achievement = model.Achievement{ID: id}
		err         error
	)

	err = gs.db.Model(&achievement).Updates(r).Error
	if err != nil {
		return Achievement{}, api.MakeServiceError(err)
	}

	return fromModel(&achievement), api.MakeServiceError(err)
}