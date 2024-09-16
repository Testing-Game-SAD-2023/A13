package playerhascategoryachievement

import (
	"strconv"

	"github.com/alarmfox/game-repository/model"
)

type PlayerHasCategoryAchievement struct {
    PlayerID            int64      `json:"playerId"`
    Category            uint8      `json:"category"`
    Progress            float64    `json:"progress"`
}

type CreateRequest struct {
    PlayerID           int64       `json:"playerId"`
    Category           uint8       `json:"category"`
    Progress           float64     `json:"progress"`
}

func (CreateRequest) Validate() error {
    return nil
}

type UpdateRequest struct {
    PlayerID           int64       `json:"playerId"`
    Category           uint8       `json:"category"`
    Progress           float64     `json:"progress"`
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

func (k KeyType) AsUint8() uint8 {
    return uint8(k)
}

func fromModel(a *model.PlayerHasCategoryAchievement) PlayerHasCategoryAchievement {
	return PlayerHasCategoryAchievement{
		PlayerID:   a.PlayerID,
		Category:   a.Category,
		Progress:   a.Progress,
	}
}