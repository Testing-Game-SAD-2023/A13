package playerhascategoryachievement

import (
	"net/http"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	Create(request *CreateRequest) (PlayerHasCategoryAchievement, error)
	FindByPID(pid int64) ([]PlayerHasCategoryAchievement, error)
	FindAll() ([]PlayerHasCategoryAchievement, error)
	Delete(pid int64, statistic string) error
	Update(pid int64, statistic string, ug *UpdateRequest) (PlayerHasCategoryAchievement, error)
}

type Controller struct {
    service Service
}

func NewController(gs Service) *Controller {
	return &Controller{service: gs}
}

func (gc *Controller) Create(w http.ResponseWriter, r *http.Request) error {

	request, err := api.FromJsonBody[CreateRequest](r.Body)

	if err != nil {
		return err
	}

	g, err := gc.service.Create(&request)

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusCreated, g)

}

func (gc *Controller) FindAll(w http.ResponseWriter, r *http.Request) error {
    g, err := gc.service.FindAll()

    if err != nil {
        return api.MakeHttpError(err)
    }

    return api.WriteJson(w, http.StatusOK, g)
}

func (gc *Controller) FindByPID(w http.ResponseWriter, r *http.Request) error {
    pid, errID := api.FromUrlParams[KeyType](r, "pid")

    if errID != nil {
        return errID
    }

    g, err := gc.service.FindByPID(pid.AsInt64())

    if err != nil {
        return api.MakeHttpError(err)
    }

    return api.WriteJson(w, http.StatusOK, g)
}

func (gc *Controller) Delete(w http.ResponseWriter, r *http.Request) error {

	pid, errID := api.FromUrlParams[KeyType](r, "pid")
	statistic, errCat := api.StringFromUrlParams(r, "statistic")

	if errID != nil {
		return errID
	}

    if errCat != nil {
        return errCat
    }

	if err := gc.service.Delete(pid.AsInt64(), string(statistic)); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}

func (gc *Controller) Update(w http.ResponseWriter, r *http.Request) error {

	pid, errID := api.FromUrlParams[KeyType](r, "pid")
	statistic, errCat := api.StringFromUrlParams(r, "statistic")

	if errID != nil {
		return errID
	}

    if errCat != nil {
        return errCat
    }

	request, err := api.FromJsonBody[UpdateRequest](r.Body)
	if err != nil {
		return err
	}

	g, err := gc.service.Update(pid.AsInt64(), string(statistic), &request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, g)
}