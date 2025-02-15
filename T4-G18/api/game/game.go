package game

import (
	"strconv"
	"time"
	
	"github.com/alarmfox/game-repository/model"
)

type Game struct {
	ID            int64      `json:"id"`
	CurrentRound  int        `json:"currentRound"`
	Username      string     `json:"username"`
	Description   string     `json:"description"`
	Difficulty    string     `json:"difficulty"`
	Score         float64    `json:"score"`
	CreatedAt     time.Time  `json:"createdAt"`
	UpdatedAt     time.Time  `json:"updatedAt"`
	StartedAt     *time.Time `json:"startedAt"`
	ClosedAt      *time.Time `json:"closedAt"`
	IsWinner      bool       `json:"isWinner"`
	Name          string     `json:"name"`
	Players       []Player   `json:"players,omitempty"`
	ScalataGameID int64      `json:"selectedScalata,omitempty"`
}

type Player struct {
	ID        int64  `json:"id"`
	AccountID string `json:"accountId"`
}

type CreateRequest struct {
	Name          string     `json:"name"`
	Players       []string   `json:"players"`
	Username      string     `json:"username"`
	Description   string     `json:"description"`
	Difficulty    string     `json:"difficulty"`
	StartedAt     *time.Time `json:"startedAt,omitempty"`
	ClosedAt      *time.Time `json:"closedAt,omitempty"`
	ScalataGameID int64      `json:"selectedScalata,omitempty"`
}

func (CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	CurrentRound int        `json:"currentRound"`
	Name         string     `json:"name"`
	Username     string     `json:"username"`
	Description  string     `json:"description"`
	Score        float64    `json:"score"`
	IsWinner     bool       `json:"isWinner"`
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
		Username:     g.Username,
		Difficulty:   g.Difficulty,
		Description:  g.Description,
		Score:        g.Score,
		CreatedAt:    g.CreatedAt,
		UpdatedAt:    g.UpdatedAt,
		Name:         g.Name,
		IsWinner:     g.IsWinner,
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
