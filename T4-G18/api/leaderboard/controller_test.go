package leaderboard

import (
	"fmt"
	"net/http"
	"net/http/httptest"
	"net/url"
	"reflect"
	"testing"

	"github.com/alarmfox/game-repository/api"
	"github.com/go-chi/chi/v5"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type LBControllerSuite struct {
	suite.Suite
	tServer *httptest.Server
}

type MockedRepository struct {
	mock.Mock
}

//Stiamo supponendo che nel repository ci sia solo le righe
// {ID: 0, PlayerID: 1, SfidaWonGames: 5, SfidaPlayedGames: 7}
// {ID: 1, PlayerID: 2, SfidaWonGames: 3, SfidaPlayedGames: 6}
// {ID: 2, PlayerID: 3, SfidaWonGames: 2, SfidaPlayedGames: 2}

func (suite *LBControllerSuite) SetupSuite() {
	fmt.Println("TEST")
	lbr := new(MockedRepository)
	//FindIntervalByPlayerID

	//matcha id 1, oppure 2, oppure 3 con parametri ben formulati
	lbr.On("FindIntervalByPlayerID", mock.MatchedBy(HelperPass), 1).
		Return(Leaderboard{[]Row{{0, 1, 7}, {1, 2, 6}, {2, 3, 2}}, 3}, nil).
		On("FindIntervalByPlayerID", mock.MatchedBy(HelperPass), 2).
		Return(Leaderboard{[]Row{{0, 1, 7}, {1, 2, 6}, {2, 3, 2}}, 3}, nil).
		On("FindIntervalByPlayerID", mock.MatchedBy(HelperPass), 3).
		Return(Leaderboard{[]Row{{0, 1, 7}, {1, 2, 6}, {2, 3, 2}}, 3}, nil).

		//matcha id 1, oppure 2, oppure 3 con parametri mal formulati

		On("FindIntervalByPlayerID", mock.MatchedBy(HelperFail), 1).
		Return(nil, api.ErrInvalidParam).
		On("FindIntervalByPlayerID", mock.MatchedBy(HelperFail), 2).
		Return(nil, api.ErrInvalidParam).
		On("FindIntervalByPlayerID", mock.MatchedBy(HelperFail), 3).
		Return(nil, api.ErrInvalidParam).

		//matcha qualsiasi id non presente con parametri ben formulati
		On("FindIntervalByPlayerID", mock.MatchedBy(HelperPass), mock.MatchedBy(func(id int) bool { return (id != 1) && (id != 2) && (id != 3) })).Return(nil, api.ErrNotFound).

		//matcha qualsiasi id non presente con parametri mal formulati

		On("FindIntervalByPlayerID", mock.MatchedBy(HelperFail), mock.MatchedBy(func(id int) bool { return (id != 1) && (id != 2) && (id != 3) })).Return(nil, api.ErrInvalidParam).

		//FindIntervalByPage

		//matcha pagina 1 con parametri ben formulati
		On("FindIntervalByPage", mock.MatchedBy(HelperPass), 1).
		Return(Leaderboard{[]Row{{0, 1, 7}, {1, 2, 6}, {2, 3, 2}}, 3}, nil).

		//match pagina 1 con parametri mal formulati

		On("FindIntervalByPage", mock.MatchedBy(HelperFail), 1).
		Return(nil, api.ErrInvalidParam).

		//matcha qualsiasi pagina non presente con parametri ben formulati
		On("FindIntervalByPage", mock.MatchedBy(HelperPass), 
        mock.MatchedBy(func(page int) bool { return page != 1 })).
		Return(nil, api.ErrNotFound).

		//matcha qualsiasi pagina non presente con parametri mal formulati

		On("FindIntervalByPage", mock.MatchedBy(HelperFail), 
        mock.MatchedBy(func(id int) bool { return id != 1 })).
		Return(nil, api.ErrInvalidParam)

	controller := NewController(lbr)

	r := chi.NewMux()
	r.Get("/{gamemode}/{statistic}", api.HandlerFunc(controller.FindByInterval))
	suite.tServer = httptest.NewServer(r)
}

func TestControllerSuite(t *testing.T) {
	suite.Run(t, new(LBControllerSuite))
}

func (suite *LBControllerSuite) TestFindIntervalByPlayerID() {

	tcs := []struct {
		Name           string
		ExpectedStatus int
		Mode           string
		Stat           string
		PageSize       string
		NumPages       string
		PlayerID       string
		StartPage      string
	}{
		{
			Name:           "T00-01-PlayerNotExists",
			ExpectedStatus: http.StatusNotFound,
			Mode:           "sfida",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "9",
			StartPage:      "",
		},
		{
			Name:           "T00-02-PlayerExists",
			ExpectedStatus: http.StatusOK,
			Mode:           "sfida",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "1",
			StartPage:      "",
		},
		{
			Name:           "T00-03-BadID",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "aaa",
			StartPage:      ""},
		{
			Name:           "T00-04-BadMode",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "1",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "1",
			StartPage:      "",
		},
		{
			Name:           "T00-05-BadStat",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "2",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "9",
			StartPage:      "",
		},
		{
			Name:           "T00-06-BadPageSize",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "2",
			PageSize:       "",
			NumPages:       "1",
			PlayerID:       "2",
			StartPage:      "",
		},
		{
			Name:           "T00-07-BadNumPages",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "2",
			PageSize:       "10",
			NumPages:       "#",
			PlayerID:       "1",
			StartPage:      "",
		},
		{
			Name:           "T00-08-PageNotExist",
			ExpectedStatus: http.StatusNotFound,
			Mode:           "sfida",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "",
			StartPage:      "3",
		},
		{
			Name:           "T00-09-PageExist",
			ExpectedStatus: http.StatusOK,
			Mode:           "sfida",
			Stat:           "partite_giocate",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "",
			StartPage:      "1",
		},
		{
			Name:           "T00-10-BadPage",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_vinte",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "",
			StartPage:      "aaa",
		},
		{
			Name:           "T00-11-BothCorrectPlayerIDStartPage",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_vinte",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "1",
			StartPage:      "1",
		},
		{
			Name:           "T00-12-NoPlayerIDNoStartPage",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_vinte",
			PageSize:       "10",
			NumPages:       "1",
			PlayerID:       "",
			StartPage:      "",
		},
        {
            Name:           "T00-13-NegativeNumPages",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_vinte",
			PageSize:       "10",
			NumPages:       "-1",
			PlayerID:       "1",
			StartPage:      "",
        },
        
            Name:           "T00-14-NegativePageSize",
			ExpectedStatus: http.StatusBadRequest,
			Mode:           "sfida",
			Stat:           "partite_vinte",
			PageSize:       "-10",
			NumPages:       "1",
			PlayerID:       "1",
			StartPage:      "",
        },
        


	}
	for _, tc := range tcs {
		tc := tc
		suite.T().Run(tc.Name, func(t *testing.T) {
			q := url.Values{}
			q.Set("pageSize", tc.PageSize)
			q.Set("numPages", tc.NumPages)
			q.Set("playerId", tc.PlayerID)
			q.Set("startPage", tc.StartPage)
			req, err := http.Get(fmt.Sprintf("%s/%s/%s?%s", suite.tServer.URL, tc.Mode, tc.Stat, q.Encode()))
			suite.NoError(err)
			suite.Equal(tc.ExpectedStatus, req.StatusCode, tc.Name)

		})
	}
}

func (gr *MockedRepository) FindIntervalByPlayerID(reader *LeaderboardReader, playerId int) (Leaderboard, error) {
	args := gr.Called(reader, playerId)
	v := args.Get(0)

	if v == nil {
		return Leaderboard{}, args.Error(1)
	}
	return v.(Leaderboard), args.Error(1)

}

func (gr *MockedRepository) FindIntervalByPage(reader *LeaderboardReader, startPage int) (Leaderboard, error) {
	args := gr.Called(reader, startPage)
	v := args.Get(0)

	if v == nil {
		return Leaderboard{}, args.Error(1)
	}
	return v.(Leaderboard), args.Error(1)
}

func HelperFail(lr *LeaderboardReader) bool {
	if (lr.mode != "sfida") && (lr.mode != "scalata") {
		return true
	}

	if (lr.stat != "partite_giocate") && (lr.stat != "partite_vinte") && (lr.stat != "classi_testate") {
		return true
	}

	if reflect.TypeOf(lr.pageSize).Kind() != reflect.Int {
		return true
	}

	if reflect.TypeOf(lr.numPages).Kind() != reflect.Int {
		return true
	}

	return false
}

func HelperPass(lr *LeaderboardReader) bool {
	if (lr.mode == "sfida" || lr.mode == "scalata") && (lr.stat == "partite_giocate" || lr.stat == "partite_vinte" || lr.stat == "classi_testate") && (reflect.TypeOf(lr.pageSize).Kind() == reflect.Int) && (reflect.TypeOf(lr.numPages).Kind() == reflect.Int ){
		return true
	}
	return false
}
