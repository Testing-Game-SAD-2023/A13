package leaderboard

import (
	"log"
	"net/http"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	FindByInterval(mode string, stat string, startPos int, endPos int) (Leaderboard, error)
}

type Controller struct {
	service Service
}

func NewController(gs Service) *Controller {
	return &Controller{service: gs}
}

func (gc *Controller) FindByInterval(w http.ResponseWriter, r *http.Request) error {
    log.Println("Leaderboard")
	gameMode, err := api.FromUrlParams[CustomString](r, "gamemode")
	if err != nil {
		return err
	}

	stat, err := api.FromUrlParams[CustomString](r, "statistic")
	if err != nil {
		return err
	}

	startPos, err := api.FromUrlParams[KeyType](r, "startPos")
	if err != nil {
		return err
	}

	endPos, err := api.FromUrlParams[KeyType](r, "endPos")
	if err != nil {
		return err
	}

	leaderboard, err := gc.service.FindByInterval(gameMode.AsString(), stat.AsString(), startPos.AsInt(), endPos.AsInt())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, leaderboard)
}
