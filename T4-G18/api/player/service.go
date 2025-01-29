package player

import (

	"gorm.io/gorm"
	"github.com/alarmfox/game-repository/model"

	
)

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{
		db: db,
	}
}

func (s *Repository) GetAllPlayers() ([]Player, error) {
    
    var modelPlayers []model.Player
    err := s.db.Find(&modelPlayers).Error
    if err != nil {
        return nil, err
    }

    // Converto i model.Player in Player
    var players []Player
    for _, mp := range modelPlayers {
        players = append(players, fromModel(&mp))
    }
    
    return players, nil
}
