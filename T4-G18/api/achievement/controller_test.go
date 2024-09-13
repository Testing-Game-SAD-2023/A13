package achievement

import (
	"net/http"
	"time"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	Create(request *CreateRequest) (Achievement, error)
	FindById(id int64) (Achievement, error)
	Delete(id int64) error
	Update(id int64, ug *UpdateRequest) (Achievement, error)
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

func (gc *Controller) FindByID(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	g, err := gc.service.FindById(id.AsInt64())

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, g)

}

func (gc *Controller) Delete(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	if err := gc.service.Delete(id.AsInt64()); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}

func (gc *Controller) Update(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	request, err := api.FromJsonBody[UpdateRequest](r.Body)
	if err != nil {
		return err
	}

	g, err := gc.service.Update(id.AsInt64(), &request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, g)
}