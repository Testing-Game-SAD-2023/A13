package round

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
	rr := new(MockedRepository)
	rr.
		On("Create", &CreateRequest{GameId: 1, TestClassId: "a.java"}).
		Return(Round{ID: 1}, nil).
		On("Create",
			mock.MatchedBy(func(r *CreateRequest) bool { return r.GameId != 1 })).
		Return(nil, api.ErrNotFound).
		On("FindById", int64(1)).
		Return(Round{ID: 1}, nil).
		On("FindById",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(nil, api.ErrNotFound).
		On("Delete", int64(1)).
		Return(nil).
		On("Delete",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(api.ErrNotFound).
		On("Update", int64(1),
			&UpdateRequest{}).
		Return(Round{}, nil).
		On("Update",
			mock.MatchedBy(func(id int64) bool { return id != 1 }),
			&UpdateRequest{}).
		Return(nil, api.ErrNotFound).
		On("FindByGame", int64(1)).
		Return([]Round{}, nil).
		On("FindByGame", mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(nil, api.ErrNotFound)

	controller := NewController(rr)

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
			Name:           "T01-01-RoundNotExists",
			ExpectedStatus: http.StatusNotFound,
			Arg:            "12",
		},
		{
			Name:           "T01-02-RoundExists",
			ExpectedStatus: http.StatusOK,
			Arg:            "1",
		},
		{
			Name:           "T01-03-BadID",
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
			Name:           "T01-04-BadJson",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"playersCount": 34`,
		},
		{
			Name:           "T01-05-RoundCreated",
			ExpectedStatus: http.StatusCreated,
			Body:           `{"gameId": 1, "testClassId": "a.java", "order": 1}`,
		},
		{
			Name:           "T01-06-GameNotExists",
			ExpectedStatus: http.StatusNotFound,
			Body:           `{"gameId": 2}`,
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
			Name:           "T01-07-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Id:             `26a4`,
		},
		{
			Name:           "T01-08-RoundDeleted",
			ExpectedStatus: http.StatusNoContent,
			Id:             `1`,
		},
		{
			Name:           "T01-09-RoundNotFound",
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
			Name:           "T01-10-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Body:           ` {"order": 1}`,
			Id:             `1a`,
		},
		{
			Name:           "T01-11-RoundUpdated",
			ExpectedStatus: http.StatusOK,
			Body:           `{"order": 2}`,
			Id:             `1`,
		},
		{
			Name:           "T01-12-InvalidJSON",
			ExpectedStatus: http.StatusBadRequest,
			Body:           ` {"order": 1t"}`,
			Id:             `1`,
		},
		{
			Name:           "T01-13-RoundNotFound",
			ExpectedStatus: http.StatusNotFound,
			Body:           ` {"order": 2}`,
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

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Input          string
	}{
		{
			Name:           "T01-14-OkList",
			ExpectedStatus: http.StatusOK,
			Input:          "1",
		},
		{
			Name:           "T01-15-InvalidId",
			ExpectedStatus: http.StatusBadRequest,
			Input:          "invalid",
		},
		{
			Name:           "T01-16-RoundNotFound",
			ExpectedStatus: http.StatusNotFound,
			Input:          "2",
		},
	}

	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			q := url.Values{}
			q.Add("gameId", tc.Input)
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

func (gr *MockedRepository) Create(r *CreateRequest) (Round, error) {
	args := gr.Called(r)
	v := args.Get(0)

	if v == nil {
		return Round{}, args.Error(1)
	}
	return v.(Round), args.Error(1)
}

func (gr *MockedRepository) FindById(id int64) (Round, error) {
	args := gr.Called(id)
	v := args.Get(0)

	if v == nil {
		return Round{}, args.Error(1)
	}
	return v.(Round), args.Error(1)

}

func (gr *MockedRepository) Delete(id int64) error {
	args := gr.Called(id)
	return args.Error(0)
}

func (gr *MockedRepository) Update(id int64, ur *UpdateRequest) (Round, error) {
	args := gr.Called(id, ur)
	v := args.Get(0)

	if v == nil {
		return Round{}, args.Error(1)
	}
	return v.(Round), args.Error(1)
}

func (gr *MockedRepository) FindByGame(id int64) ([]Round, error) {
	args := gr.Called(id)
	v := args.Get(0)

	if v == nil {
		return nil, args.Error(1)
	}
	return v.([]Round), args.Error(1)

}
