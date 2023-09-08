package game

import (
	"net/http"
	"time"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	Create(request *CreateRequest) (Game, error)
	FindById(id int64) (Game, error)
	Delete(id int64) error
	Update(id int64, ug *UpdateRequest) (Game, error)
	FindByInterval(accountId string, i api.IntervalParams, p api.PaginationParams) ([]Game, int64, error)
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

func (gc *Controller) List(w http.ResponseWriter, r *http.Request) error {
	accountId, err := api.FromUrlQuery[AccountIdType](r, "accountId", "")

	if err != nil {
		return err
	}
	page, err := api.FromUrlQuery[KeyType](r, "page", 1)

	if err != nil {
		return err
	}

	pageSize, err := api.FromUrlQuery[KeyType](r, "pageSize", 10)

	if err != nil {
		return err
	}

	startDate, err := api.FromUrlQuery(r, "startDate", IntervalType(time.Now().Add(-24*time.Hour)))

	if err != nil {
		return err
	}

	endDate, err := api.FromUrlQuery(r, "endDate", IntervalType(time.Now()))

	if err != nil {
		return err
	}

	ip := api.IntervalParams{
		Start: startDate.AsTime(),
		End:   endDate.AsTime(),
	}

	pp := api.PaginationParams{
		Page:     page.AsInt64(),
		PageSize: pageSize.AsInt64(),
	}

	games, count, err := gc.service.FindByInterval(accountId.AsString(), ip, pp)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, api.MakePaginatedResponse(games, count, pp))
}
