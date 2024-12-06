package leaderboard

import "strconv"

type Row struct {
	UserID int64 `json:"userId"`
	Stat   int32 `json:"statistic"`
}

type Leaderboard struct {
	Positions   []Row `json:"positions"`
	TotalLength int64 `json:"totalLength"`
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

func (k KeyType) AsInt() int {
	return int(k)
}
