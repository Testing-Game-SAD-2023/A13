package round

import (
	"strconv"
	"time"
	"github.com/alarmfox/game-repository/model"
	"github.com/alarmfox/game-repository/api/robot"
	"fmt"
)

type Round struct {
	ID          int64      `json:"id"`
	Order       int        `json:"order"`
	TestClassId string     `json:"testClassId"`
	GameID      int64      `json:"gameId"`
	RobotID     int64      `json:"robotId"`
	Robot       robot.Robot      `json:"robot"`
	CreatedAt   time.Time  `json:"createdAt"`
	UpdatedAt   time.Time  `json:"updatedAt"`
	StartedAt   *time.Time `json:"startedAt"`
	ClosedAt    *time.Time `json:"closedAt"`
}

type CreateRequest struct {
	GameId      int64      `json:"gameId"`
	TestClassId string     `json:"testClassId"`
	RobotID     int64      `json:"robotId"` // ID del Robot associato
	StartedAt   *time.Time `json:"startedAt,omitempty"`
	ClosedAt    *time.Time `json:"closedAt,omitempty"`
}

func (r CreateRequest) Validate() error {
    if r.RobotID == 0 {
        return fmt.Errorf("robotId is required")
    }
    return nil
}

type UpdateRequest struct {
	StartedAt *time.Time `json:"startedAt,omitempty"`
	ClosedAt  *time.Time `json:"closedAt,omitempty"`
	RobotID   *int64     `json:"robotId,omitempty"`
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

func fromModel(r *model.Round) Round {
	return Round{
		ID:          r.ID,
		Order:       r.Order,
		CreatedAt:   r.CreatedAt,
		UpdatedAt:   r.UpdatedAt,
		TestClassId: r.TestClassId,
		StartedAt:   r.StartedAt,
		ClosedAt:    r.ClosedAt,
		GameID:      r.GameID,
		RobotID:     r.RobotID, // Associa l'ID del Robot
	}
}
