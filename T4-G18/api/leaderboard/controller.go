package leaderboard

import (
	"net/http"
	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	FindIntervalByPlayerID(reader *LeaderboardReader, playerId int) (Leaderboard, error)
	FindIntervalByPage(reader *LeaderboardReader, startPage int) (Leaderboard, error)
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
        return api.MakeHttpError(api.ErrInvalidParam)
	}

	stat, err := api.FromUrlParams[CustomString](r, "statistic")
	if err != nil {
        return api.MakeHttpError(api.ErrInvalidParam)
	}

	//Parametri obbligatorio, se almeno uno è 0 termina
	pageSize, err := api.FromUrlQuery[KeyType](r, "pageSize", 0)
	if err != nil {
		return api.MakeHttpError(api.ErrInvalidParam)
	}


	numPages, err := api.FromUrlQuery[KeyType](r, "numPages", 0)
	if err != nil {
        return api.MakeHttpError(api.ErrInvalidParam)
	}


	if (pageSize == 0) || (numPages == 0) {
		return api.MakeHttpError(api.ErrInvalidParam)
	}

	//Parametri mutuamente esclusivi, necessario almeno uno in quanto usati per gestire la ricerca della risorsa

	playerId, err := api.FromUrlQuery[KeyType](r, "playerId", -1)
	if err != nil {
        return api.MakeHttpError(api.ErrInvalidParam)
	}


	startPage, err := api.FromUrlQuery[KeyType](r, "startPage", 0)
	if err != nil {
        return api.MakeHttpError(api.ErrInvalidParam)
	}

	if ((startPage <= 0) && (playerId <= -1)) || ((startPage > 0) && (playerId > -1)) {
		return api.MakeHttpError(api.ErrInvalidParam)
	}

	reader := LeaderboardReader{mode: string(gamemode), stat: string(stat), pageSize: int(pageSize), numPages: int(numPages)}

	var leaderboard Leaderboard

	if playerId >= 0 {
		leaderboard, err = gc.service.FindIntervalByPlayerID(&reader, int(playerId))
	} else {
		leaderboard, err = gc.service.FindIntervalByPage(&reader, int(startPage))
	}

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, leaderboard)
}
