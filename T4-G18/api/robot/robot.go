package robot

import (
	"encoding/json"
	"fmt"
	"strings"
	"time"

	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
)

type Robot struct {
	ID          int64     `json:"id"`
	TestClassId string    `json:"testClassId"`
	CreatedAt   time.Time `json:"createdAt"`
	UpdatedAt   time.Time `json:"updatedAt"`
	Difficulty  string    `json:"difficulty"`
	Type        RobotType `json:"type"`
	Scores      string    `json:"scores"`
}
type RobotType int8

const (
	randoop RobotType = iota
	evosuite
)

func (rb RobotType) Parse(s string) (RobotType, error) {
	switch strings.ToLower(s) {
	case randoop.String():
		return randoop, nil
	case evosuite.String():
		return evosuite, nil
	default:
		return RobotType(0), fmt.Errorf("%w: unsupported test engine",
			api.ErrInvalidParam)
	}
}

func (rb RobotType) String() string {
	switch rb {
	case randoop:
		return "randoop"
	case evosuite:
		return "evosuite"
	default:
		panic("unreachable")
	}
}

func (rb RobotType) MarshalJSON() ([]byte, error) {
	return json.Marshal(rb.String())
}

func (rb *RobotType) UnmarshalJSON(data []byte) error {
	var s string
	if err := json.Unmarshal(data, &s); err != nil {
		return err
	}
	v, err := rb.Parse(s)
	if err != nil {
		return err
	}

	*rb = v
	return nil

}

func (rb RobotType) AsInt8() int8 {
	return int8(rb)
}

type CreateSingleRequest struct {
	TestClassId string    `json:"testClassId"`
	Scores      string    `json:"scores"`
	Difficulty  string    `json:"difficulty"`
	Type        RobotType `json:"type"`
}

func (r CreateSingleRequest) Validate() error {
	return nil
}

type CreateRequest struct {
	Robots []CreateSingleRequest `json:"robots"`
}

func (robots CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	Scores     string `json:"scores"`
	Difficulty string `json:"difficulty"`
}

func (UpdateRequest) Validate() error {
	return nil
}

type CustomString string

// CustomString is a dummy type that implements Convertable and Validable interfaces
func (CustomString) Parse(s string) (CustomString, error) {
	return CustomString(s), nil
}

func (s CustomString) AsString() string {
	return string(s)
}

func (CustomString) Validate() error {
	return nil
}

func fromModel(r *model.Robot) *Robot {
	return &Robot{
		ID:          r.ID,
		CreatedAt:   r.CreatedAt,
		UpdatedAt:   r.UpdatedAt,
		TestClassId: r.TestClassId,
		Difficulty:  r.Difficulty,
		Type:        RobotType(r.Type),
		Scores:      r.Scores,
	}
}
