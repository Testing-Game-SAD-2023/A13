package round

import (
	"net/http"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	Create(request *CreateRequest) (Round, error)
	FindById(id int64) (Round, error)
	Delete(id int64) error
	Update(id int64, request *UpdateRequest) (Round, error)
	FindByGame(id int64) ([]Round, error)
}

type Controller struct {
	service Service
}

func NewController(rs Service) *Controller {
	return &Controller{
		service: rs,
	}
}

func (rc *Controller) Create(w http.ResponseWriter, r *http.Request) error {

	request, err := api.FromJsonBody[CreateRequest](r.Body)
	if err != nil {
		return err
	}

	round, err := rc.service.Create(&request)

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusCreated, round)

}

func (rc *Controller) Update(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	request, err := api.FromJsonBody[UpdateRequest](r.Body)
	if err != nil {
		return err
	}

	round, err := rc.service.Update(id.AsInt64(), &request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, round)
}

func (rc *Controller) FindByID(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	round, err := rc.service.FindById(id.AsInt64())

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, round)

}

func (rh *Controller) Delete(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	if err := rh.service.Delete(id.AsInt64()); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}

func (rc *Controller) List(w http.ResponseWriter, r *http.Request) error {
	id, err := api.FromUrlQuery(r, "gameId", KeyType(10))

	if err != nil {
		return err
	}

	rounds, err := rc.service.FindByGame(id.AsInt64())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, rounds)
}
