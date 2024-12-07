package leaderboard

import (
	"fmt"

	"github.com/alarmfox/game-repository/api"
	"gorm.io/gorm"
)

// Mapping dei parametro URL "mode" e "stat" sulla colonna del DB, i valori delle chiavi devono coincidere con i valori
// definiti nella documentazione dell'API
var modeMap = map[string]string{
	"sfida":   "sfida",
	"scalata": "scalata",
}

var statMap = map[string]string{
	"partite_giocate": "played_games",
	"partite_vinte":   "won_games",
	"classi_testate":  "tested_classes",
}

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
		rows        []Row
		leaderboard Leaderboard
		totalLength int64
	)

	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, mode, stat)

	if !ok {
		return leaderboard, api.ErrInvalidParam
	}

	err := gs.db.Table("player_stats").
		Select("player_id AS user_id, ? AS stat", columnName).
		Order(fmt.Sprintf("%s DESC", columnName)).
		Offset(startPos - 1).
		Limit(endPos - startPos).
		Scan(&rows).Error

	if err != nil {
		return leaderboard, api.MakeServiceError(err)
	}

	err = gs.db.Table("player_stats").Count(&totalLength).Error

	if err != nil {
		return leaderboard, api.MakeServiceError(err)
	}

	leaderboard.Positions = rows
	leaderboard.TotalLength = totalLength

	return leaderboard, nil
}

func (gs *Repository) FindPlayerPosition(mode string, stat string, playerId int) (int, error) {

	var position int

	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, mode, stat)

	if !ok {
		return position, api.ErrInvalidParam
	}

	query := fmt.Sprintf(`
		SELECT ROW_NUMBER() OVER (ORDER BY %s) AS rn
		FROM player_stats
		WHERE player_id = ?
	`, columnName)

	err := gs.db.Raw(query, playerId).Scan(&position).Error

	if err != nil {
		return position, err
	}

	return position, nil
}
