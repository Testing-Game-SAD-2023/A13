package turn

import (
	"fmt"
	"io"
	"net/http"
	"os"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	CreateBulk(request *CreateRequest) ([]Turn, error)
	FindById(id int64) (Turn, error)
	Delete(id int64) error
	Update(id int64, request *UpdateRequest) (Turn, error)
	FindByRound(id int64) ([]Turn, error)
	SaveFile(id int64, r io.Reader) error
	GetFile(id int64) (string, *os.File, error)
}

type Controller struct {
	service Service
}

func NewController(service Service) *Controller {
	return &Controller{
		service: service,
	}
}

func (tc *Controller) Create(w http.ResponseWriter, r *http.Request) error {

	request, err := api.FromJsonBody[CreateRequest](r.Body)
	if err != nil {
		return err
	}
	turns, err := tc.service.CreateBulk(&request)

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusCreated, turns)
}

func (tc *Controller) Update(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	request, err := api.FromJsonBody[UpdateRequest](r.Body)
	if err != nil {
		return err
	}

	turn, err := tc.service.Update(id.AsInt64(), &request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, turn)
}

func (tc *Controller) FindByID(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	turn, err := tc.service.FindById(id.AsInt64())

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, turn)

}

func (tc *Controller) Delete(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	if err := tc.service.Delete(id.AsInt64()); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}

func (tc *Controller) Upload(w http.ResponseWriter, r *http.Request) error {

	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	if err := tc.service.SaveFile(id.AsInt64(), r.Body); err != nil {
		return api.MakeHttpError(err)
	}
	defer r.Body.Close()

	w.WriteHeader(http.StatusOK)
	return nil
}

func (tc *Controller) Download(w http.ResponseWriter, r *http.Request) error {
	id, err := api.FromUrlParams[KeyType](r, "id")
	if err != nil {
		return err
	}

	fname, f, err := tc.service.GetFile(id.AsInt64())
	if err != nil {
		return api.MakeHttpError(err)
	}
	defer f.Close()

	w.Header().Set("Content-Type", "application/zip")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=%s", fname))
	if _, err := io.Copy(w, f); err != nil {
		return err
	}
	return nil
}

func (tc *Controller) List(w http.ResponseWriter, r *http.Request) error {
	id, err := api.FromUrlQuery(r, "roundId", KeyType(10))

	if err != nil {
		return err
	}
	turns, err := tc.service.FindByRound(id.AsInt64())
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, turns)
}
