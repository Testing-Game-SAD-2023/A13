package game

import (
	"bytes"
	"fmt"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"

	"github.com/alarmfox/game-repository/api"
	"github.com/go-chi/chi/v5"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type ControllerSuite struct {
	suite.Suite
	tServer *httptest.Server
}

func (suite *ControllerSuite) SetupSuite() {
	gr := new(MockedRepository)
	gr.
		On("Create", &CreateRequest{Name: ""}).
		Return(Game{ID: 1}, nil).
		On("FindById", int64(1)).
		Return(Game{ID: 1}, nil).
		On("FindById",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(nil, api.ErrNotFound).
		On("Delete", int64(1)).
		Return(nil).
		On("Delete",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(api.ErrNotFound).
		On("Update", int64(1),
			&UpdateRequest{Name: "test", CurrentRound: 10}).
		Return(Game{}, nil).
		On("Update",
			mock.MatchedBy(func(id int64) bool { return id != 1 }),
			&UpdateRequest{Name: "test", CurrentRound: 10}).
		Return(nil, api.ErrNotFound).
		On("FindByInterval", mock.Anything, mock.Anything).
		Return([]Game{}, int(64), nil).
		On("FindByPlayer", mock.Anything, mock.Anything).
		Return([]Game{}, int(64), nil)
	controller := NewController(gr)

	r := chi.NewMux()
	r.Get("/{id}", api.HandlerFunc(controller.FindByID))
	r.Get("/", api.HandlerFunc(controller.List))
	r.Post("/", api.HandlerFunc(controller.Create))
	r.Delete("/{id}", api.HandlerFunc(controller.Delete))
	r.Put("/{id}", api.HandlerFunc(controller.Update))

	suite.tServer = httptest.NewServer(r)
}

func (suite *ControllerSuite) TestFindByID() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Arg            string
	}{
		{
			Name:           "T00-01-GameNotExists",
			ExpectedStatus: http.StatusNotFound,
			Arg:            "12",
		},
		{
			Name:           "T00-02-GameExists",
			ExpectedStatus: http.StatusOK,
			Arg:            "1",
		},
		{
			Name:           "T00-03-BadID",
			ExpectedStatus: http.StatusBadRequest,
			Arg:            "aaa",
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			req, err := http.Get(fmt.Sprintf("%s/%s", suite.tServer.URL, tc.Arg))
			suite.NoError(err)

			suite.Equal(tc.ExpectedStatus, req.StatusCode, tc.Name)

		})
	}
}

func (suite *ControllerSuite) TestCreate() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Body           string
	}{
		{
			Name:           "T00-04-BadJson",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"playersCount": 34`,
		},
		{
			Name:           "T00-05-GameCreated",
			ExpectedStatus: http.StatusCreated,
			Body:           `{"playersCount": 10}`,
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			req, err := http.Post(suite.tServer.URL,
				"application/json",
				bytes.NewBufferString(tc.Body))

			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, req.StatusCode, tc.Name)

		})
	}

}

func (suite *ControllerSuite) TestDelete() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Id             string
	}{
		{
			Name:           "T00-06-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Id:             `26a4`,
		},
		{
			Name:           "T00-07-GameDeleted",
			ExpectedStatus: http.StatusNoContent,
			Id:             `1`,
		},
		{
			Name:           "T00-08-GameNotFound",
			ExpectedStatus: http.StatusNotFound,
			Id:             `14`,
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			url := fmt.Sprintf("%s/%s", suite.tServer.URL, tc.Id)
			req, err := http.NewRequest(http.MethodDelete, url, nil)
			suite.NoError(err)

			res, err := http.DefaultClient.Do(req)
			suite.NoError(err)

			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

		})
	}

}
func (suite *ControllerSuite) TestUpdate() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Body           string
		Id             string
	}{
		{
			Name:           "T00-09-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Body:           ` {"currentRound": 10, "name: "test"}`,
			Id:             `1a`,
		},
		{
			Name:           "T00-10-GameUpdated",
			ExpectedStatus: http.StatusOK,
			Body:           `{"currentRound": 10, "name": "test"}`,
			Id:             `1`,
		},
		{
			Name:           "T00-11-InvalidJSON",
			ExpectedStatus: http.StatusBadRequest,
			Body:           ` {"currentRound": 10, "name: "test"}`,
			Id:             `1`,
		},
		{
			Name:           "T00-12-GameNotFound",
			ExpectedStatus: http.StatusNotFound,
			Body:           ` {"currentRound": 10, "name": "test"}`,
			Id:             `11`,
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			url := fmt.Sprintf("%s/%s", suite.tServer.URL, tc.Id)
			req, err := http.NewRequest(http.MethodPut,
				url,
				bytes.NewBufferString(tc.Body))

			suite.NoError(err)
			res, err := http.DefaultClient.Do(req)
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

		})
	}

}
func (suite *ControllerSuite) TestList() {

	type input struct {
		Page      string
		PageSize  string
		StartDate string
		EndDate   string
	}

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Input          input
	}{
		{
			Name:           "Valid input",
			ExpectedStatus: http.StatusOK,
			Input: input{
				Page:      "1",
				PageSize:  "10",
				StartDate: "2023-01-01",
				EndDate:   "2023-01-31",
			},
		},
		{
			Name:           "Invalid page",
			ExpectedStatus: http.StatusBadRequest,
			Input: input{
				Page:      "invalid",
				PageSize:  "10",
				StartDate: "2023-01-01",
				EndDate:   "2023-01-31",
			},
		},
		{
			Name:           "Invalid pageSize",
			ExpectedStatus: http.StatusBadRequest,
			Input: input{
				Page:      "1",
				PageSize:  "invalid",
				StartDate: "2023-01-01",
				EndDate:   "2023-01-31",
			},
		},
		{
			Name:           "Invalid startDate",
			ExpectedStatus: http.StatusBadRequest,
			Input: input{
				Page:      "1",
				PageSize:  "10",
				StartDate: "invalid",
				EndDate:   "2023-01-31",
			},
		},
		{
			Name:           "Invalid endDate",
			ExpectedStatus: http.StatusBadRequest,
			Input: input{
				Page:      "1",
				PageSize:  "10",
				StartDate: "2023-01-01",
				EndDate:   "invalid",
			},
		},
	}

	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			q := url.Values{}
			q.Set("page", tc.Input.Page)
			q.Set("pageSize", tc.Input.PageSize)
			q.Set("startDate", tc.Input.StartDate)
			q.Set("endDate", tc.Input.EndDate)
			url := fmt.Sprintf("%s?%s", suite.tServer.URL, q.Encode())
			res, err := http.Get(url)
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

			defer res.Body.Close()
		})
	}

}

func TestControllerSuite(t *testing.T) {
	suite.Run(t, new(ControllerSuite))
}

func (suite *ControllerSuite) TearDownSuite() {
	defer suite.tServer.Close()
}

type MockedRepository struct {
	mock.Mock
}

func (gr *MockedRepository) Create(r *CreateRequest) (Game, error) {
	args := gr.Called(r)
	v := args.Get(0)

	if v == nil {
		return Game{}, args.Error(1)
	}
	return v.(Game), args.Error(1)
}

func (gr *MockedRepository) FindById(id int64) (Game, error) {
	args := gr.Called(id)
	v := args.Get(0)

	if v == nil {
		return Game{}, args.Error(1)
	}
	return v.(Game), args.Error(1)

}

func (gr *MockedRepository) Delete(id int64) error {
	args := gr.Called(id)
	return args.Error(0)
}

func (gr *MockedRepository) Update(id int64, ur *UpdateRequest) (Game, error) {
	args := gr.Called(id, ur)
	v := args.Get(0)

	if v == nil {
		return Game{}, args.Error(1)
	}
	return v.(Game), args.Error(1)
}

func (gr *MockedRepository) FindByInterval(accountId string, i api.IntervalParams, p api.PaginationParams) ([]Game, int64, error) {
	args := gr.Called(i, p)
	v := args.Get(0)

	if v == nil {
		return nil, int64(args.Int(1)), args.Error(2)
	}
	return v.([]Game), int64(args.Int(1)), args.Error(2)

}
