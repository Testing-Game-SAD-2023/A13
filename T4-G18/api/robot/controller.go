package robot

import (
	"net/http"

	"github.com/alarmfox/game-repository/api"
)

type Service interface {
	CreateBulk(request *CreateRequest) (int, error)
	FindByFilter(testClassId string, difficulty string, t RobotType) (Robot, error)
	DeleteByTestClass(testClassId string) error
}

type Controller struct {
	service Service
}

func NewController(rs Service) *Controller {
	return &Controller{
		service: rs,
	}
}

func (rc *Controller) CreateBulk(w http.ResponseWriter, r *http.Request) error {

	request, err := api.FromJsonBody[CreateRequest](r.Body)
	if err != nil {
		return err
	}
	n, err := rc.service.CreateBulk(&request)
	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusCreated, map[string]any{"created": n})
}

func (rc *Controller) FindByFilter(w http.ResponseWriter, r *http.Request) error {

	testClassId, err := api.FromUrlQuery[CustomString](r, "testClassId", "")
	if err != nil {
		return err
	}

	difficulty, err := api.FromUrlQuery[CustomString](r, "difficulty", "")
	if err != nil {
		return err
	}

	t, err := api.FromUrlQuery[RobotType](r, "type", 0)
	if err != nil {
		return err
	}

	robot, err := rc.service.FindByFilter(
		testClassId.AsString(),
		difficulty.AsString(),
		t,
	)

	if err != nil {
		return api.MakeHttpError(err)
	}

	return api.WriteJson(w, http.StatusOK, robot)

}

func (rc *Controller) Delete(w http.ResponseWriter, r *http.Request) error {
	testClassId, err := api.FromUrlQuery[CustomString](r, "testClassId", "")
	if err != nil {
		return err
	}
	if err := rc.service.DeleteByTestClass(testClassId.AsString()); err != nil {
		return api.MakeHttpError(err)
	}
	w.WriteHeader(http.StatusNoContent)
	return nil
}
