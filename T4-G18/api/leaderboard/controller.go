package leaderboard

import (
	"github.com/alarmfox/game-repository/api"
	"net/http"
)

type Service interface {
	FindByInterval(mode string, stat string, startPos int, endPos int) (Leaderboard, error)
	FindPlayerPosition(mode string, stat string, playerId int) (int, error)
}

type Controller struct {
	service Service
}

func NewController(gs Service) *Controller {
	return &Controller{service: gs}
}

func (gc *Controller) FindByInterval(w http.ResponseWriter, r *http.Request) error {
 
	gamemode, err := api.FromUrlParams[CustomString](r, "gamemode")
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


	leaderboard, err := gc.service.FindByInterval(gamemode.AsString(), stat.AsString(), startPos.AsInt(), endPos.AsInt())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, leaderboard)
}

func (gc *Controller) FindPlayerPosition(w http.ResponseWriter, r *http.Request) error {
	gameMode, err := api.FromUrlParams[CustomString](r, "gamemode")
	if err != nil {
		return err
	}

	stat, err := api.FromUrlParams[CustomString](r, "statistic")
	if err != nil {
		return err
	}

	playerId, err := api.FromUrlParams[KeyType](r, "playerID")
	if err != nil {
		return err
	}

	row, err := gc.service.FindPlayerPosition(gameMode.AsString(), stat.AsString(), playerId.AsInt())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, row)
}
