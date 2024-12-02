package leaderboard

import (
	"github.com/alarmfox/game-repository/api"
	"net/http"
)

type Service interface {
	FindByInterval(mode string, stat string, startPos int64, endPos int64) (Leaderboard, error)
}

type Controller struct {
	service Service
}

func NewController(gs Service) *Controller {
	return &Controller{service: gs}
}

func (gc *Controller) FindByInterval(w http.ResponseWriter, r *http.Request) error {
	gameMode, err := api.FromUrlParams[CustomString](r, "gameMode")
	if err != nil {
		return err
	}

	stat, err := api.FromUrlParams[CustomString](r, "statistic")
	if err != nil {
		return err
	}

	startPos, err := api.FromUrlParams[KeyType](r, "startPosition")
	if err != nil {
		return err
	}

	endPos, err := api.FromUrlParams[KeyType](r, "endPosition")
	if err != nil {
		return err
	}

	leaderboard, err := gc.service.FindByInterval(gameMode.AsString(), stat.AsString(), startPos.AsInt64(), endPos.AsInt64())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, leaderboard)
}
