package achievement

import (
	"strconv"

	"github.com/alarmfox/game-repository/model"
)

type Achievement struct {
    ID                 int64       `json:"id"`                  // Achievement ID
    Name               string      `json:"name"`                // Achievement name: mandatory
    Category           uint8       `json:"category"`
    ProgressRequired   float64     `json:"progressRequired"`    // Progress required to obtain the achievement
}

type AchievementProgress struct {
    ID                 int64       `json:"id"`
    Name               string      `json:"name"`
    ProgressRequired   float64     `json:"progressRequired"`
    Progress           float64     `json:"progress"`
}

type CreateRequest struct {
    Name               string      `json:"name"`
    Category           uint8       `json:"category"`
    ProgressRequired   float64     `json:"progressRequired"`
}

func (CreateRequest) Validate() error {
    return nil
}

type UpdateRequest struct {
    ID                 int64       `json:"id"`
    Name               string      `json:"name"`
    Category           uint8       `json:"category"`
    ProgressRequired   float64     `json:"progressRequired"`
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

func fromModel(a *model.Achievement) Achievement {
	return Achievement{
		ID:                 a.ID,
		Name:               a.Name,
		Category:           a.Category,
		ProgressRequired:   a.ProgressRequired,
	}
}