package model

import (
	"database/sql"
	"time"
	"gorm.io/gorm"
)


type PlayerHasCategoryAchievement struct {
    PlayerID            int64      `gorm:"primaryKey;autoIncrement:false"`
    Category            string     `gorm:"primaryKey;autoIncrement:false"`
    Progress            float64    `gorm:"default:0"`                      // Current progress
}

func (PlayerHasCategoryAchievement) TableName() string {
    return "player_has_category_achievement"
}

type ScalataGame struct {
	ID           int64      `gorm:"primaryKey;autoIncrement"`
	PlayerID     int64      `gorm:"not null"` // This will store the AccountID of the player
	ScalataName  string     `gorm:"not null"`
	Games        []Game     `gorm:"foreignKey:ScalataGameID;constraint:OnDelete:CASCADE;"`
	CreatedAt    time.Time  `gorm:"autoCreateTime"`
	UpdatedAt    time.Time  `gorm:"autoUpdateTime"`
	StartedAt    *time.Time `gorm:"default:null"`
	ClosedAt     *time.Time `gorm:"default:null"`
	FinalScore   float64    `gorm:"default:0"`
	IsFinished   bool       `gorm:"default:false"`
	CurrentRound int        `gorm:"default:1"`
}

func (ScalataGame) TableName() string {
	return "scalata_games"
}

type Game struct {
	ID            int64          `gorm:"primaryKey;autoIncrement"`
	Name          string         `gorm:"default:null"`
	Username      string         `gorm:"default:null"`
	CurrentRound  int            `gorm:"default:1"`
	Description   string         `gorm:"default:null"`
	Difficulty    string         `gorm:"default:null"`
	Score         float64        `gorm:"default:0"`
	IsWinner      bool           `gorm:"default:false"`
	CreatedAt     time.Time      `gorm:"autoCreateTime"`
	UpdatedAt     time.Time      `gorm:"autoUpdateTime"`
	StartedAt     *time.Time     `gorm:"default:null"`
	ClosedAt      *time.Time     `gorm:"default:null"`
	Rounds        []Round        `gorm:"foreignKey:GameID;constraint:OnDelete:CASCADE;"`
	Players       []Player       `gorm:"many2many:player_games;foreignKey:ID;joinForeignKey:GameID;References:AccountID;joinReferences:PlayerID"`
	ScalataGameID int64          `gorm:"default:null"`
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
	Points 	  float64 	`gorm:"default:0"`
	GamesWon int64  	`gorm:"default:0"`
	CreatedAt time.Time `gorm:"autoCreateTime"`
	UpdatedAt time.Time `gorm:"autoUpdateTime"`
	Turns     []Turn    `gorm:"foreignKey:PlayerID;constraint:OnDelete:SET NULL;"`
	Games     []Game    `gorm:"many2many:player_games;foreignKey:AccountID;joinForeignKey:PlayerID;"`
	// ScalataGames []ScalataGame `gorm:"foreignKey:PlayerID;constraint:OnDelete:SET NULL;"`
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
	Order     int        `gorm:"not null;default:1"`
	CreatedAt time.Time  `gorm:"autoCreateTime"`
	UpdatedAt time.Time  `gorm:"autoUpdateTime"`
	StartedAt *time.Time `gorm:"default:null"`
	ClosedAt  *time.Time `gorm:"default:null"`
	Metadata  Metadata   `gorm:"foreignKey:TurnID;constraint:OnDelete:SET NULL;"`
	Scores    string     `gorm:"default:null"`
	PlayerID  int64      `gorm:"not null"`
	RoundID   int64      `gorm:"not null"`
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

func (g *Game) AfterSave(tx *gorm.DB) (err error) {
    // Ricarica i giocatori associati
    var players []Player
    if err := tx.Model(g).Association("Players").Find(&players); err != nil {
        return err
    }

    // Logica per aggiornare i dati dei giocatori
    for _, player := range players {
        // Ad esempio, aggiornare il campo Punti in base al punteggio del gioco
        player.Points += g.Score // Supponendo che "Score" sia il punteggio che contribuisce ai punti
        if err := tx.Save(&player).Error; err != nil {
            return err
        }
    }

    return nil
}
