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

func (gs *Repository) FindIntervalByPlayerID(reader *LeaderboardReader, playerId int) (Leaderboard, error) {
	var (
		rows           []Row
		leaderboard    Leaderboard
		playerPosition int
		totalLength    int64
		err            error
	)

	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, reader.mode, reader.stat)

	if !ok {
		return leaderboard, api.ErrInvalidParam
	}

	err = gs.db.Transaction(func(tx *gorm.DB) error {
		query := fmt.Sprintf(`
		       SELECT position FROM(
		               SELECT *, row_number() OVER (ORDER BY %s DESC, player_id DESC) AS position FROM player_stats
		               )
		               AS sub_q WHERE player_id = ?`, columnName)

		err := gs.db.Raw(query, fmt.Sprintf("%d", playerId)).Take(&playerPosition).Error

		if err != nil {
			return err
		}

		startPage := (playerPosition / reader.pageSize) + 1		// 16+1
		offset := (startPage - 1) * reader.pageSize
		limit := reader.pageSize * reader.numPages

		query = buildQuery(columnName)
		err = gs.db.Raw(query, limit, offset).Scan(&rows).Error
		if err != nil {
			return err
		}

		if rows == nil {
			return api.ErrNotFound
		}

		return err
	})

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

func (gs *Repository) FindIntervalByPage(reader *LeaderboardReader, startPage int) (Leaderboard, error) {
	var (
		rows        []Row
		leaderboard Leaderboard
		totalLength int64
	)

	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, reader.mode, reader.stat)

	if !ok {
		return leaderboard, api.ErrInvalidParam
	}

	query := buildQuery(columnName)

	offset := ((startPage - 1) * reader.pageSize)
	limit := reader.pageSize * reader.numPages

	err := gs.db.Raw(query, limit, offset).Scan(&rows).Error

	if err != nil {
		return leaderboard, api.MakeServiceError(err)
	}

	if rows == nil {
		return leaderboard, api.ErrNotFound
	}

	err = gs.db.Table("player_stats").Count(&totalLength).Error

	if err != nil {
		return leaderboard, api.MakeServiceError(err)
	}

	leaderboard.Positions = rows
	leaderboard.TotalLength = totalLength

	return leaderboard, nil
}

func buildQuery(columnName string) string {
	query := fmt.Sprintf(`
    SELECT * FROM (SELECT 
        player_id AS user_id,
        %s AS stat,
        ROW_NUMBER() OVER (ORDER BY %s DESC, player_id DESC) AS rank
    FROM player_stats) AS sub_q
    LIMIT ? OFFSET ?`, columnName, columnName)
	return query
}
