package player 
 
import ( 
 "net/http" 
 
 "github.com/alarmfox/game-repository/api" 
) 
type Service interface{ 
 GetAllPlayers() ([]Player, error) 
} 
type Controller struct { 
 service Service 
} 
 
func NewController(ps Service) *Controller { 
 return &Controller{service: ps} 
} 
 
func (pc *Controller) GetAllPlayers(w http.ResponseWriter, r *http.Request) error { 
 players, err := pc.service.GetAllPlayers() 
 
 if err!= nil{ 
  return api.MakeHttpError(err) 
 } 
 
 return api.WriteJson(w, http.StatusOK, players) 
}
