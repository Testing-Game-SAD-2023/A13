package game

import (
	"strconv"
	"time"

	"github.com/alarmfox/game-repository/model"
)

type Game struct {
	ID           int64      `json:"id"`
	CurrentRound int        `json:"currentRound"`
	Description  string     `json:"description"`
	Difficulty   string     `json:"difficulty"`
	CreatedAt    time.Time  `json:"createdAt"`
	UpdatedAt    time.Time  `json:"updatedAt"`
	StartedAt    *time.Time `json:"startedAt"`
	ClosedAt     *time.Time `json:"closedAt"`
	Name         string     `json:"name"`
	Players      []Player   `json:"players,omitempty"`
}

type Player struct {
	ID        int64  `json:"id"`
	AccountID string `json:"accountId"`
}

type CreateRequest struct {
	Name        string     `json:"name"`
	Players     []string   `json:"players"`
	Description string     `json:"description"`
	Difficulty  string     `json:"difficulty"`
	StartedAt   *time.Time `json:"startedAt,omitempty"`
	ClosedAt    *time.Time `json:"closedAt,omitempty"`
}

func (CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	CurrentRound int        `json:"currentRound"`
	Name         string     `json:"name"`
	Description  string     `json:"description"`
	StartedAt    *time.Time `json:"startedAt,omitempty"`
	ClosedAt     *time.Time `json:"closedAt,omitempty"`
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
func fromModel(g *model.Game) Game {
	return Game{
		ID:           g.ID,
		CurrentRound: g.CurrentRound,
		Difficulty:   g.Difficulty,
		Description:  g.Description.String,
		CreatedAt:    g.CreatedAt,
		UpdatedAt:    g.UpdatedAt,
		Name:         g.Name,
		StartedAt:    g.StartedAt,
		ClosedAt:     g.ClosedAt,
		Players:      parsePlayers(g.Players),
	}

}

func parsePlayers(players []model.Player) []Player {
	res := make([]Player, len(players))
	for i, player := range players {
		res[i] = Player{
			ID:        player.ID,
			AccountID: player.AccountID,
		}
	}
	return res
}
