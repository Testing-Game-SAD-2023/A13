package player 
 
import ( 
 "time" 
 
 "github.com/alarmfox/game-repository/model" 
) 
 
type Player struct{ 
  ID            int64      `json:"id"`
  AccountID     string     `json:"accountID"`
  CreatedAt     time.Time  `json:"createdAt"`
  UpdatedAt     time.Time  `json:"updatedAt"`
  Points        float64    `json:"points"`
  GamesWon      int64      `json:"gamesWon"` 
} 

func fromModel(p *model.Player) Player { 
 return Player{ 
  ID:           p.ID, 
  AccountID:   p.AccountID,      
  CreatedAt:   p.CreatedAt,      
  UpdatedAt:   p.UpdatedAt,      
  Points:       p.Points, 
  GamesWon:    p.GamesWon, 
 } 
 
}