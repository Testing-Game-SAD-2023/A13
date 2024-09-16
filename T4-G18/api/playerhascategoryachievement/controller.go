package playerhascategoryachievement

import (
	"net/http"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	Create(request *CreateRequest) (PlayerHasCategoryAchievement, error)
	FindAll() ([]PlayerHasCategoryAchievement, error)
	Delete(pid int64, category uint8) error
	Update(pid int64, category uint8, ug *UpdateRequest) (PlayerHasCategoryAchievement, error)
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

func (gc *Controller) Delete(w http.ResponseWriter, r *http.Request) error {

	pid, errID := api.FromUrlParams[KeyType](r, "pid")
	category, errCat := api.FromUrlParams[KeyType](r, "category")

	if errID != nil {
		return errID
	}

    if errCat != nil {
        return errCat
    }

	if err := gc.service.Delete(pid.AsInt64(), category.AsUint8()); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}

func (gc *Controller) Update(w http.ResponseWriter, r *http.Request) error {

	pid, errID := api.FromUrlParams[KeyType](r, "pid")
	category, errCat := api.FromUrlParams[KeyType](r, "category")

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

	g, err := gc.service.Update(pid.AsInt64(), category.AsUint8(), &request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, g)
}