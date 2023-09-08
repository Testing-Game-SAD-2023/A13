package model

import (
	"database/sql"
	"time"
)

type Game struct {
	CurrentRound int   `gorm:"default:1"`
	ID           int64 `gorm:"primaryKey;autoIncrement"`
	Name         string
	Description  sql.NullString `gorm:"default:null"`
	Difficulty   string
	CreatedAt    time.Time  `gorm:"autoCreateTime"`
	UpdatedAt    time.Time  `gorm:"autoUpdateTime"`
	StartedAt    *time.Time `gorm:"default:null"`
	ClosedAt     *time.Time `gorm:"default:null"`
	Rounds       []Round    `gorm:"foreignKey:GameID;constraint:OnDelete:CASCADE;"`
	Players      []Player   `gorm:"many2many:player_games;foreignKey:ID;joinForeignKey:GameID;References:AccountID;joinReferences:PlayerID"`
}

func (Game) TableName() string {
	return "games"
}

type PlayerGame struct {
	PlayerID  string    `gorm:"primaryKey"`
	GameID    int64     `gorm:"primaryKey"`
	CreatedAt time.Time `gorm:"autoCreateTime"`
	UpdatedAt time.Time `gorm:"autoUpdateTime"`
	IsWinner  bool      `gorm:"default:false"`
}

func (PlayerGame) TableName() string {
	return "player_games"
}

type Player struct {
	ID        int64     `gorm:"primaryKey;autoIncrement"`
	AccountID string    `gorm:"unique"`
	CreatedAt time.Time `gorm:"autoCreateTime"`
	UpdatedAt time.Time `gorm:"autoUpdateTime"`
	Turns     []Turn    `gorm:"foreignKey:PlayerID;constraint:OnDelete:SET NULL;"`
	Games     []Game    `gorm:"many2many:player_games;foreignKey:AccountID;joinForeignKey:PlayerID;"`
}

func (Player) TableName() string {
	return "players"
}

type Round struct {
	ID          int64      `gorm:"primaryKey;autoIncrement"`
	Order       int        `gorm:"not null;default:1"`
	StartedAt   *time.Time `gorm:"default:null"`
	ClosedAt    *time.Time `gorm:"default:null"`
	UpdatedAt   time.Time  `gorm:"autoUpdateTime"`
	CreatedAt   time.Time  `gorm:"autoCreateTime"`
	Turns       []Turn     `gorm:"foreignKey:RoundID;constraint:OnDelete:CASCADE;"`
	TestClassId string     `gorm:"not null"`
	GameID      int64      `gorm:"not null"`
}

func (Round) TableName() string {
	return "rounds"
}

type Turn struct {
	ID        int64      `gorm:"primaryKey;autoIncrement"`
	CreatedAt time.Time  `gorm:"autoCreateTime"`
	UpdatedAt time.Time  `gorm:"autoUpdateTime"`
	StartedAt *time.Time `gorm:"default:null"`
	ClosedAt  *time.Time `gorm:"default:null"`
	Metadata  Metadata   `gorm:"foreignKey:TurnID;constraint:OnDelete:SET NULL;"`
	Scores    string     `gorm:"default:null"`
	IsWinner  bool       `gorm:"default:false"`
	PlayerID  int64      `gorm:"index:idx_playerturn,unique;not null"`
	RoundID   int64      `gorm:"index:idx_playerturn,unique;not null"`
}

func (Turn) TableName() string {
	return "turns"
}

type Metadata struct {
	ID        int64         `gorm:"primaryKey;autoIncrement"`
	CreatedAt time.Time     `gorm:"autoCreateTime"`
	UpdatedAt time.Time     `gorm:"autoUpdateTime"`
	TurnID    sql.NullInt64 `gorm:"unique"`
	Path      string        `gorm:"unique;not null"`
}

func (Metadata) TableName() string {
	return "metadata"
}

type Robot struct {
	ID          int64     `gorm:"primaryKey;autoIncrement"`
	CreatedAt   time.Time `gorm:"autoCreateTime"`
	UpdatedAt   time.Time `gorm:"autoUpdateTime"`
	TestClassId string    `gorm:"not null;index:idx_robotquery"`
	Scores      string    `gorm:"default:null"`
	Difficulty  string    `gorm:"not null;index:idx_robotquery"`
	Type        int8      `gorm:"not null;index:idx_robotquery"`
}

func (Robot) TableName() string {
	return "robots"
}
