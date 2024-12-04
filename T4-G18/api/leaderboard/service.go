package leaderboard

import (
	"fmt"

	"github.com/alarmfox/game-repository/api"
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

func (gs *Repository) FindByInterval(mode string, stat string, startPos int, endPos int) (Leaderboard, error) {
	var (
        rows []Row
		leaderboard Leaderboard
		totalLength int64
	)

	columnName := fmt.Sprintf("%s_%s", mode, stat)

	err := gs.db.Table("player_stats").
		Select(fmt.Sprintf("player_id AS user_id, %s AS stat", columnName)).
		Order(fmt.Sprintf("%s DESC", columnName)).
		Offset(startPos).
		Limit(endPos - startPos).
		Scan(&rows).Error

	if err != nil {
		return leaderboard, err
	}

	err = gs.db.Table("player_stats").Count(&totalLength).Error

	if err != nil {
		return leaderboard, api.MakeServiceError(err)
	}

    leaderboard.Positions = rows
    leaderboard.TotalLength = totalLength

	return leaderboard, nil
}
