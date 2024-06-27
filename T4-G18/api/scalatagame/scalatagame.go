package scalatagame

import (
	"strconv"
	"time"

	"github.com/alarmfox/game-repository/model"
)

type ScalataGame struct {
	ID           int64      `json:"id"`
	PlayerID     int64      `json:"playerID"`
	ScalataName  string     `json:"scalataName"`
	Games        []Game     `json:"games,omitempty"`
	CreatedAt    time.Time  `json:"createdAt"`
	UpdatedAt    time.Time  `json:"updatedAt"`
	StartedAt    *time.Time `json:"startedAt"`
	ClosedAt     *time.Time `json:"closedAt"`
	FinalScore   float64    `json:"finalScore"`
	IsFinished   bool       `json:"isFinished"`
	CurrentRound int        `json:"currentRound"`
}

type Player struct {
	ID        int64  `json:"id"`
	AccountID string `json:"accountId"`
}

type Game struct {
	ID int64 `json:"id"`
}

type CreateRequest struct {
	ScalataName string    `json:"scalataName"`
	PlayerID    int64     `json:"playerID"`
	CreatedAt   time.Time `json:"createdAt"`
}

func (CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	CurrentRound int        `json:"currentRound"`
	StartedAt    *time.Time `json:"startedAt,omitempty"`
	UpdatedAt    time.Time  `json:"updatedAt,omitempty"`
	ClosedAt     *time.Time `json:"closedAt,omitempty"`
	FinalScore   float64    `json:"finalScore,omitempty"`
	IsFinished   bool       `json:"isFinished,omitempty"`
}

func (UpdateRequest) Validate() error {
	return nil
}

type KeyType int64

func (c KeyType) Parse(s string) (KeyType, error) {
	a, err := strconv.ParseInt(s, 10, 64)
	return KeyType(a), err
}

func (k KeyType) AsInt64() int64 {
	return int64(k)
}

type IntervalType time.Time

func (IntervalType) Parse(s string) (IntervalType, error) {
	t, err := time.Parse(time.DateOnly, s)
	return IntervalType(t), err
}

func (k IntervalType) AsTime() time.Time {
	return time.Time(k)
}

type AccountIdType string

func (AccountIdType) Parse(s string) (AccountIdType, error) {
	return AccountIdType(s), nil
}

func (a AccountIdType) AsString() string {
	return string(a)
}
func fromModel(sg *model.ScalataGame) ScalataGame {
	return ScalataGame{
		ID:           sg.ID,
		PlayerID:     sg.PlayerID,
		ScalataName:  sg.ScalataName,
		CreatedAt:    sg.CreatedAt,
		UpdatedAt:    sg.UpdatedAt,
		StartedAt:    sg.StartedAt,
		ClosedAt:     sg.ClosedAt,
		FinalScore:   sg.FinalScore,
		IsFinished:   sg.IsFinished,
		CurrentRound: sg.CurrentRound,
		Games:        parsePlayerGames(sg.Games),
	}
}

func parsePlayerGames(playerGames []model.Game) []Game {
	res := make([]Game, len(playerGames))
	for i, playerGame := range playerGames {
		res[i] = Game{
			ID: playerGame.ID,
		}
	}
	return res
}
