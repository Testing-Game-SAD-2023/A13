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


//FindIntervalByPlayerID(reader LeaderboardReader, playerId int) (Leaderboard, error)

func (gs *Repository)  FindIntervalByPage(reader LeaderboardReader, startPage int) (Leaderboard, error){
	var (
		rows        []Row
		leaderboard Leaderboard
		totalLength int64
	)

	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, reader.mode, reader.stat)

	if !ok {
		return leaderboard, api.ErrInvalidParam
	}

    offset := (startPage * reader.pageSize) -1
    limit := reader.pageSize * reader.numPages

	err := gs.db.Table("player_stats").
		Select(fmt.Sprintf("player_id AS user_id, %s AS stat", columnName)).
		Order(fmt.Sprintf("%s DESC", columnName)).
		Offset(offset).
		Limit(limit).
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

func (gs *Repository) FindPlayerPosition(mode string, stat string, playerId int) (PlayerPosition, error) {

	var position PlayerPosition
	columnName, ok := api.GetLeaderboardColName(modeMap, statMap, mode, stat)

	if !ok {
		return position, api.ErrInvalidParam
	}

	query := fmt.Sprintf(`
        SELECT position FROM(
                SELECT *, row_number() OVER (ORDER BY %s DESC) AS position FROM player_stats
                )
                AS sub_q WHERE player_id = ?`, columnName)

	// playerId convertito in stringa per mantenere la coerenza con il tipo in PlayerStats (model)
	// scelta obbligata dal tipo di PlayerGames...

	err := gs.db.Raw(query, fmt.Sprintf("%d", playerId)).Scan(&position.Position).Error

	if err != nil {
		return position, err
	}

    if position.Position == 0{
        return position, api.ErrNotFound
    }

	return position, nil
}
