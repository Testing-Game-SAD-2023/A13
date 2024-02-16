package turn

import (
	"archive/zip"
	"bytes"
	"fmt"
	"io"
	"net/http"
	"net/http/httptest"
	"net/url"
	"os"
	"testing"

	"github.com/alarmfox/game-repository/api"
	"github.com/go-chi/chi/v5"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type ControllerSuite struct {
	suite.Suite
	tServer *httptest.Server
	tmpDir  string
}

func (suite *ControllerSuite) SetupSuite() {
	tr := new(MockedRepository)
	f, err := os.CreateTemp(suite.tmpDir, "")
	suite.NoError(err)
	_, err = f.Write([]byte("hello"))
	suite.NoError(err)

	tr.
		On("GetFile",
			int64(1)).
		Return(f.Name(), f, nil).
		On("GetFile",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return("", nil, api.ErrNotFound).
		On("SaveFile",
			int64(1),
			mock.Anything).
		Return(nil).
		On("SaveFile",
			mock.MatchedBy(func(id int64) bool { return id != 1 }),
			mock.Anything).
		Return(api.ErrNotFound).
		On("CreateBulk", &CreateRequest{RoundId: 1}).
		Return([]Turn{}, nil).
		On("CreateBulk", mock.MatchedBy(func(r *CreateRequest) bool { return r.RoundId != 1 })).
		Return(nil, api.ErrNotFound).
		On("FindById", int64(1)).
		Return(Turn{ID: 1}, nil).
		On("FindById",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(nil, api.ErrNotFound).
		On("Delete", int64(1)).
		Return(nil).
		On("Delete",
			mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(api.ErrNotFound).
		On("Update", int64(1), &UpdateRequest{IsWinner: true, Scores: "a"}).
		Return(Turn{}, nil).
		On("Update", mock.MatchedBy(func(id int64) bool { return id != 1 }),
			&UpdateRequest{IsWinner: true, Scores: "a"}).
		Return(nil, api.ErrNotFound).
		On("FindByRound", int64(1)).
		Return([]Turn{}, nil).
		On("FindByRound", mock.MatchedBy(func(id int64) bool { return id != 1 })).
		Return(nil, api.ErrNotFound)

	suite.tmpDir = os.TempDir()
	controller := NewController(tr)

	r := chi.NewMux()

	r.Get("/{id}/files", api.HandlerFunc(controller.Download))
	r.Put("/{id}/files", api.HandlerFunc(controller.Upload))
	r.Post("/", api.HandlerFunc(controller.Create))
	r.Get("/", api.HandlerFunc(controller.List))
	r.Put("/{id}", api.HandlerFunc(controller.Update))
	r.Delete("/{id}", api.HandlerFunc(controller.Delete))
	r.Get("/{id}", api.HandlerFunc(controller.FindByID))

	suite.tServer = httptest.NewServer(r)
}

func (suite *ControllerSuite) TestFindByID() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Arg            string
	}{
		{
			Name:           "T02-01-TurnNotExists",
			ExpectedStatus: http.StatusNotFound,
			Arg:            "12",
		},
		{
			Name:           "T02-02-TurnExists",
			ExpectedStatus: http.StatusOK,
			Arg:            "1",
		},
		{
			Name:           "T02-03-BadID",
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
			Name:           "T02-04-BadJson",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"roundId": 34`,
		},
		{
			Name:           "T02-05-TurnCreated",
			ExpectedStatus: http.StatusCreated,
			Body:           `{"roundId": 1, "playerId": 1}`,
		},
		{
			Name:           "T02-06-RoundNotExists",
			ExpectedStatus: http.StatusNotFound,
			Body:           `{"turnId": 2}`,
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
			Name:           "T02-07-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Id:             `26a4`,
		},
		{
			Name:           "T02-08-TurnDeleted",
			ExpectedStatus: http.StatusNoContent,
			Id:             `1`,
		},
		{
			Name:           "T02-09-TurnNotFound",
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
			Name:           "T02-10-BadId",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"scores": "a", "isWinner": true}`,
			Id:             `1a`,
		},
		{
			Name:           "T02-11-TurnUpdated",
			ExpectedStatus: http.StatusOK,
			Body:           `{"scores": "a", "isWinner": true}`,
			Id:             `1`,
		},
		{
			Name:           "T02-12-InvalidJSON",
			ExpectedStatus: http.StatusBadRequest,
			Body:           `{"order"}`,
			Id:             `1`,
		},
		{
			Name:           "T02-13-TurnNotFound",
			ExpectedStatus: http.StatusNotFound,
			Body:           `{"scores": "a", "isWinner": true}`,
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
			Name:           "T02-14-TurnsFound",
			ExpectedStatus: http.StatusOK,
			Input:          "1",
		},
		{
			Name:           "T02-15-ErrBadId",
			ExpectedStatus: http.StatusBadRequest,
			Input:          "invalid",
		},
		{
			Name:           "T02-16-RoundNotFound",
			ExpectedStatus: http.StatusNotFound,
			Input:          "2",
		},
	}

	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			q := url.Values{}
			q.Add("roundId", tc.Input)
			url := fmt.Sprintf("%s?%s", suite.tServer.URL, q.Encode())
			res, err := http.Get(url)
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

			defer res.Body.Close()
		})
	}

}

func (suite *ControllerSuite) TestUpload() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		TurnID         string
		Body           io.Reader
	}{
		{
			Name:           "T21-BadZip",
			ExpectedStatus: http.StatusOK,
			TurnID:         "1",
			Body:           bytes.NewBufferString("not a zip"),
		},
		{
			Name:           "T22-ZipSaved",
			ExpectedStatus: http.StatusOK,
			TurnID:         "1",
			Body:           generateValidZipContent(suite.T(), []byte("hello")),
		},
		{
			Name:           "T23-TurnNotFound",
			ExpectedStatus: http.StatusNotFound,
			TurnID:         "12",
			Body:           generateValidZipContent(suite.T(), []byte("hello")),
		},
		{
			Name:           "T24-BadTurnID",
			ExpectedStatus: http.StatusBadRequest,
			TurnID:         "a12",
			Body:           generateValidZipContent(suite.T(), []byte("hell")),
		},
	}

	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {

			req, err := http.NewRequest(http.MethodPut,
				fmt.Sprintf("%s/%s/files",
					suite.tServer.URL,
					tc.TurnID),
				tc.Body)

			suite.NoError(err)

			res, err := http.DefaultClient.Do(req)
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

		})
	}
}

func (suite *ControllerSuite) TestDownload() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		TurnID         string
	}{
		{
			Name:           "T35-DownloadOK",
			ExpectedStatus: http.StatusOK,
			TurnID:         "1",
		},
		{
			Name:           "T36-TurnNotFound",
			ExpectedStatus: http.StatusNotFound,
			TurnID:         "21",
		},
	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {

			res, err := http.Get(fmt.Sprintf("%s/%s/files", suite.tServer.URL, tc.TurnID))
			suite.NoError(err)
			defer res.Body.Close()

			_, err = io.Copy(io.Discard, res.Body)

			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, res.StatusCode, tc.Name)

		})
	}
}

func (suite *ControllerSuite) TearDownSuite() {
	defer os.RemoveAll(suite.tmpDir)
	defer suite.tServer.Close()
}
func TestTurnControllerSuite(t *testing.T) {
	suite.Run(t, new(ControllerSuite))
}

type MockedRepository struct {
	mock.Mock
}

func (m *MockedRepository) CreateBulk(request *CreateRequest) ([]Turn, error) {
	args := m.Called(request)
	v := args.Get(0)

	if v == nil {
		return nil, args.Error(1)
	}
	return v.([]Turn), args.Error(1)
}

func (m *MockedRepository) FindById(id int64) (Turn, error) {
	args := m.Called(id)
	v := args.Get(0)

	if v == nil {
		return Turn{}, args.Error(1)
	}
	return v.(Turn), args.Error(1)
}

func (m *MockedRepository) Delete(id int64) error {
	args := m.Called(id)
	return args.Error(0)
}

func (m *MockedRepository) FindByRound(id int64) ([]Turn, error) {
	args := m.Called(id)
	v := args.Get(0)

	if v == nil {
		return nil, args.Error(1)
	}
	return v.([]Turn), args.Error(1)
}

func (m *MockedRepository) Update(id int64, request *UpdateRequest) (Turn, error) {
	args := m.Called(id, request)
	v := args.Get(0)

	if v == nil {
		return Turn{}, args.Error(1)
	}
	return v.(Turn), args.Error(1)
}

func (m *MockedRepository) SaveFile(id int64, r io.Reader) error {
	args := m.Called(id, r)
	return args.Error(0)
}

func (m *MockedRepository) GetFile(id int64) (string, *os.File, error) {
	args := m.Called(id)
	v := args.Get(1)

	if v == nil {
		return "", nil, args.Error(2)
	}
	return args.String(0), v.(*os.File), args.Error(2)
}

func generateValidZipContent(t *testing.T, content []byte) io.Reader {
	buf := new(bytes.Buffer)
	zipWriter := zip.NewWriter(buf)

	// Create a file inside the zip archive
	fileWriter, err := zipWriter.Create("file.txt")
	if err != nil {
		t.Fatal(err)
	}

	// Write some content to the file
	_, err = fileWriter.Write([]byte(content))
	if err != nil {
		t.Fatal(err)
	}

	// Close the zip writer
	err = zipWriter.Close()
	if err != nil {
		t.Fatal(err)
	}

	return buf
}
