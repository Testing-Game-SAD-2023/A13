package leaderboard

import "strconv"

type Row struct {
	UserID int64
	Stat   int64
}

type Leaderboard struct {
	Positions   []Row
	TotalLenght int64
}

type CustomString string

func (CustomString) Parse(s string) (CustomString, error) {
	return CustomString(s), nil
}

func (s CustomString) AsString() string {
	return string(s)
}

type KeyType int64

func (c KeyType) Parse(s string) (KeyType, error) {
	a, err := strconv.ParseInt(s, 10, 64)
	return KeyType(a), err
}

func (k KeyType) AsInt64() int64 {
	return int64(k)
}

// func fromModel(g *model.Game) Game {
// 	return Game{
// 		ID:           g.ID,
// 		CurrentRound: g.CurrentRound,
// 		Username:     g.Username,
// 		Difficulty:   g.Difficulty,
// 		Description:  g.Description,
// 		Score:        g.Score,
// 		CreatedAt:    g.CreatedAt,
// 		UpdatedAt:    g.UpdatedAt,
// 		Name:         g.Name,
// 		IsWinner:     g.IsWinner,
// 		StartedAt:    g.StartedAt,
// 		ClosedAt:     g.ClosedAt,
// 		Players:      parsePlayers(g.Players),
// 	}
//
// }
