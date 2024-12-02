package leaderboard

import (
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

func (gs *Repository) FindByInterval(mode string, stat string, startPos int64, endPos int64) (Leaderboard, error)
