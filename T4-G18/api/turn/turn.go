package turn

import (
	"strconv"
	"time"

	"github.com/alarmfox/game-repository/model"
)

type Turn struct {
	ID        int64      `json:"id"`
	IsWinner  bool       `json:"isWinner"`
	CreatedAt time.Time  `json:"createdAt"`
	UpdatedAt time.Time  `json:"updatedAt"`
	PlayerID  int64      `json:"playerId"`
	RoundID   int64      `json:"roundId"`
	Scores    string     `json:"scores"`
	StartedAt *time.Time `json:"startedAt"`
	ClosedAt  *time.Time `json:"closedAt"`
}
type CreateRequest struct {
	RoundId   int64      `json:"roundId"`
	Players   []string   `json:"players"`
	StartedAt *time.Time `json:"startedAt,omitempty"`
	ClosedAt  *time.Time `json:"closedAt,omitempty"`
}

func (CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	Scores    string     `json:"scores"`
	IsWinner  bool       `json:"isWinner"`
	StartedAt *time.Time `json:"startedAt,omitempty"`
	ClosedAt  *time.Time `json:"closedAt,omitempty"`
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

func fromModel(t *model.Turn) Turn {
	return Turn{
		ID:        t.ID,
		IsWinner:  t.IsWinner,
		Scores:    t.Scores,
		CreatedAt: t.CreatedAt,
		UpdatedAt: t.UpdatedAt,
		PlayerID:  t.PlayerID,
		StartedAt: t.StartedAt,
		ClosedAt:  t.ClosedAt,
		RoundID:   t.RoundID,
	}
}
