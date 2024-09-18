package playerhascategoryachievement

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

func (gs *Repository) Create(r *CreateRequest) (PlayerHasCategoryAchievement, error) {
    var (
        phca = model.PlayerHasCategoryAchievement{
            PlayerID:           r.PlayerID,
            Category:           r.Category,
            Progress:           r.Progress,
        }
    )

    err := gs.db.Transaction(func(tx *gorm.DB) error {
        return tx.Create(&phca).Error
    })

	if err != nil {
		return PlayerHasCategoryAchievement{}, api.MakeServiceError(err)
	}

    return fromModel(&phca), nil
}

func (gs *Repository) FindAll() ([]PlayerHasCategoryAchievement, error) {
    var phcas []model.PlayerHasCategoryAchievement

    err := gs.db.
        Find(&phcas).
        Error

    res := make([]PlayerHasCategoryAchievement, len(phcas))
	for i, phca := range phcas {
		res[i] = fromModel(&phca)
	}

    return res, api.MakeServiceError(err)
}

func (gs *Repository) FindByPID(pid int64) ([]PlayerHasCategoryAchievement, error) {
    var phcas []model.PlayerHasCategoryAchievement

    err := gs.db.
        Where(&model.PlayerHasCategoryAchievement{PlayerID: pid}).
        Find(&phcas).
        Error

    res := make([]PlayerHasCategoryAchievement, len(phcas))
	for i, phca := range phcas {
		res[i] = fromModel(&phca)
	}

    return res, api.MakeServiceError(err)
}

func (gs *Repository) Delete(pid int64, category uint8) error {
	db := gs.db.
		Where(&model.PlayerHasCategoryAchievement{PlayerID: pid, Category: category}).
		Delete(&model.PlayerHasCategoryAchievement{})

	if db.Error != nil {
		return api.MakeServiceError(db.Error)
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}
	return nil
}

func (gs *Repository) Update(pid int64, category uint8, r *UpdateRequest) (PlayerHasCategoryAchievement, error) {

	var (
		phca model.PlayerHasCategoryAchievement = model.PlayerHasCategoryAchievement{PlayerID: pid, Category: category}
		err         error
	)

	err = gs.db.Model(&phca).Updates(r).Error
	if err != nil {
		return PlayerHasCategoryAchievement{}, api.MakeServiceError(err)
	}

	return fromModel(&phca), api.MakeServiceError(err)
}