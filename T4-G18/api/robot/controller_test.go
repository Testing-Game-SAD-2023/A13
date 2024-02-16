package robot

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

type RobotControllerSuite struct {
	suite.Suite
	tServer *httptest.Server
}

func (suite *RobotControllerSuite) SetupSuite() {
	tr := new(MockedRobotRepository)

	tr.
		On("CreateBulk", mock.Anything).
		Return(1, nil).
		On("FindByFilter", mock.Anything, mock.Anything, mock.Anything).
		Return(Robot{ID: 1}, nil).
		On("DeleteByTestClass", "a.java").
		Return(nil).
		On("DeleteByTestClass",
			mock.MatchedBy(func(id string) bool { return id != "a.java" })).
		Return(api.ErrNotFound)

	controller := NewController(tr)

	r := chi.NewMux()

	r.Post("/", api.HandlerFunc(controller.CreateBulk))
	r.Get("/", api.HandlerFunc(controller.FindByFilter))
	r.Delete("/", api.HandlerFunc(controller.Delete))

	suite.tServer = httptest.NewServer(r)
}
func (suite *RobotControllerSuite) TearDownSuite() {
	defer suite.tServer.Close()
}
func TestRobotControllerSuite(t *testing.T) {
	suite.Run(t, new(RobotControllerSuite))
}

func (suite *RobotControllerSuite) TestFindByFilter() {
	type input struct {
		TestClassId string
		Difficulty  string
		RobotType   string
	}
	tcs := []struct {
		Name           string
		ExpectedStatus int
		Input          input
	}{
		{
			Name:           "T04-01-ValidInput",
			ExpectedStatus: http.StatusOK,
			Input: input{
				TestClassId: "TestRobot.java",
				Difficulty:  "easy",
				RobotType:   "randoop",
			},
		},
		{
			Name:           "T04-02-ValidInput",
			ExpectedStatus: http.StatusOK,
			Input: input{
				TestClassId: "SomeOtherRobot.java",
				Difficulty:  "medium",
				RobotType:   "evosuite",
			},
		},
		{
			Name:           "T04-03-InvalidInput",
			ExpectedStatus: http.StatusBadRequest,
			Input: input{
				TestClassId: "InvalidRobot.java",
				Difficulty:  "hard",
				RobotType:   "randoop.",
			},
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			q := url.Values{}
			q.Set("testClassId", tc.Input.TestClassId)
			q.Set("difficulty", tc.Input.Difficulty)
			q.Set("type", tc.Input.RobotType)

			req, err := http.Get(fmt.Sprintf("%s?%s", suite.tServer.URL, q.Encode()))
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, req.StatusCode, tc.Name)

		})
	}
}

func (suite *RobotControllerSuite) TestCreate() {
	tcs := []struct {
		Name           string
		ExpectedStatus int
		Body           string
	}{
		{
			Name:           "T04-04-ValidInput",
			ExpectedStatus: http.StatusCreated,
			Body:           `{"robots": [{"testClassId": "a.java", "scores": "some scores", "difficulty": "some difficulty", "type": "randoop"}]}`,
		},
		{
			Name:           "T04-05-InvalidRobotType",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"robots": [{"testClassId": "a.java", "scores": "some scores", "difficulty": "some difficulty", "type": "ranop"}]}`,
		},
		{
			Name:           "T04-06-MissingField",
			ExpectedStatus: http.StatusCreated,
			Body:           `{"robots": [{"testClassId": "a.java", "scores": "some scores", "difficulty": "some difficulty"}]}`,
		},
		{
			Name:           "T04-07-BadlyFormattedJSON",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"robots: [{"testClassId": "a.java", "scores": "some scores", "difficulty": "some difficulty", "type": "evosuite}]}`,
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

func (suite *RobotControllerSuite) TestDelete() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		TestClassId    string
	}{
		{
			Name:           "T04-08-Ok",
			ExpectedStatus: http.StatusNoContent,
			TestClassId:    `a.java`,
		},
		{
			Name:           "T04-09-NotFound",
			ExpectedStatus: http.StatusNotFound,
			TestClassId:    `b.java`,
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			p := url.Values{}
			p.Add("testClassId", tc.TestClassId)
			url := fmt.Sprintf("%s?%s", suite.tServer.URL, p.Encode())
			req, err := http.NewRequest(http.MethodDelete, url, nil)
			suite.NoError(err)

			res, err := http.DefaultClient.Do(req)
			suite.NoError(err)

			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

		})
	}

}

type MockedRobotRepository struct {
	mock.Mock
}

func (m *MockedRobotRepository) CreateBulk(request *CreateRequest) (int, error) {
	args := m.Called(request)

	return args.Int(0), args.Error(1)
}

func (m *MockedRobotRepository) FindByFilter(testClassId string, difficulty string, t RobotType) (Robot, error) {
	args := m.Called(testClassId, difficulty, t)
	v := args.Get(0)

	if v == nil {
		return Robot{}, args.Error(1)
	}
	return v.(Robot), args.Error(1)

}
func (m *MockedRobotRepository) DeleteByTestClass(testClassId string) error {
	args := m.Called(testClassId)
	return args.Error(0)
}
