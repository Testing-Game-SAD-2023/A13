package leaderboard

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

type LbControllerSuite struct {
	suite.Suite
	tServer *httptest.Server
}


func (suite *LbControllerSuite) SetupSuite() {
	lbr := new(MockedLeaderBoardRepository)
	lbr.On("FindIntervalByPlayerID", &LeaderboardReader{}).
    Return(Leaderboard{}).
    On().
    Return("FindIntervalByPag")
}

type MockedLeaderBoardRepository struct {
	mock.Mock
}
