package turn

import (
	"bytes"
	"database/sql"
	"errors"
	"io"
	"os"
	"path"
	"testing"

	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"github.com/stretchr/testify/suite"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
	"gorm.io/gorm/logger"
)

type RepositorySuite struct {
	suite.Suite
	db       *gorm.DB
	testPath string
	service  Repository
}

func (suite *RepositorySuite) SetupSuite() {
	dbUrl := os.Getenv("DB_URI")
	db, err := gorm.Open(postgres.Open(dbUrl), &gorm.Config{
		SkipDefaultTransaction: true,
		TranslateError:         true,
		Logger:                 logger.Discard,
	})

	if err != nil {
		suite.T().Fatal(err)
	}

	suite.db = db

	err = db.AutoMigrate(
		&model.Game{},
		&model.Round{},
		&model.Player{},
		&model.Turn{},
		&model.Metadata{},
	)
	if err != nil {
		suite.T().Fatal(err)
	}

	suite.testPath = path.Join(os.TempDir(), "testdata")
	if err := os.Mkdir(suite.testPath, os.ModePerm); err != nil && !errors.Is(err, os.ErrExist) {
		suite.T().Fatal(err)
	}

	suite.service = *NewRepository(db, suite.testPath)
}

func (suite *RepositorySuite) Cleanup() {
	suite.T().Helper()
	// Truncate each table individually
	if err := suite.db.Exec("TRUNCATE TABLE turns RESTART IDENTITY CASCADE").Error; err != nil {
		suite.T().Fatal(err)
	}
	if err := suite.db.Exec("TRUNCATE TABLE metadata RESTART IDENTITY CASCADE").Error; err != nil {
		suite.T().Fatal(err)
	}
	if err := suite.db.Exec("TRUNCATE TABLE rounds RESTART IDENTITY CASCADE").Error; err != nil {
		suite.T().Fatal(err)
	}
	if err := suite.db.Exec("TRUNCATE TABLE players RESTART IDENTITY CASCADE").Error; err != nil {
		suite.T().Fatal(err)
	}
	if err := suite.db.Exec("TRUNCATE TABLE games RESTART IDENTITY CASCADE").Error; err != nil {
		suite.T().Fatal(err)
	}

}

func (suite *RepositorySuite) SeedTestData() {
	// Create a game with rounds and turns
	suite.T().Helper()

	// Create a test game
	game := model.Game{
		Name: "Test Game",
		Rounds: []model.Round{
			{
				Order:       1,
				TestClassId: "test",
				Turns: []model.Turn{
					{
						PlayerID: 1,          // Replace with your desired player ID
						Scores:   "10,20,30", // Replace with your desired scores
					},
					// Add more turns as needed
				},
			},
			// Add more rounds as needed
		},
	}

	// Create a player
	player := model.Player{
		AccountID: "testplayer", // Replace with your desired account ID
	}

	// Create test metadata
	metadata := model.Metadata{
		Path: path.Join(suite.testPath, "1.zip"), // Replace with your desired path
	}

	// Create a player-game relationship
	playerGame := model.PlayerGame{
		PlayerID: "testplayer", // Replace with the player ID
		GameID:   1,            // Replace with the game ID
	}

	// Save the test data to the database
	err := suite.db.Transaction(func(tx *gorm.DB) error {

		// Create the player
		if err := tx.Clauses(clause.OnConflict{DoNothing: true}).Create(&player).Error; err != nil {
			return err
		}
		// Create the game
		if err := tx.Clauses(clause.OnConflict{DoNothing: true}).Create(&game).Error; err != nil {
			return err
		}
		metadata.TurnID = sql.NullInt64{Valid: true, Int64: 1}
		// Create the metadata
		if err := tx.Clauses(clause.OnConflict{DoNothing: true}).Create(&metadata).Error; err != nil {
			return err
		}
		// Create the player-game relationship
		if err := tx.Clauses(clause.OnConflict{DoNothing: true}).Create(&playerGame).Error; err != nil {
			return err
		}

		return nil
	})

	// Check for errors during data seeding
	if err != nil {
		suite.T().Fatalf("Failed to seed test data: %v", err)
	}

	// Create test file
	f, err := os.Create(metadata.Path)
	if err != nil {
		suite.T().Fatalf("Failed to create %s: %v", metadata.Path, err)
	}
	defer f.Close()
	if _, err := f.Write([]byte("some file content")); err != nil {
		suite.T().Fatal(err)
	}
}

func (suite *RepositorySuite) TestSaveFile() {
	type input struct {
		turnId  int64
		content io.Reader
	}

	type output struct {
		err error
	}

	tcs := []struct {
		Name   string
		Output output
		Input  input
	}{
		{
			Name:   "T51-NotAZip",
			Output: output{err: api.ErrNotAZip},
			Input: input{
				turnId:  1,
				content: bytes.NewBufferString("hello"),
			},
		},
		{
			Name:   "T52-SuccessfulSave",
			Output: output{err: nil},
			Input: input{
				turnId:  1,
				content: generateValidZipContent(suite.T(), []byte("hello")),
			},
		},
		{
			Name:   "T53-EmptyFile",
			Output: output{err: nil},
			Input: input{
				turnId:  1,
				content: generateValidZipContent(suite.T(), []byte(nil)),
			},
		},
		{
			Name:   "T54-InvalidTurnID",
			Output: output{err: api.ErrNotFound},
			Input: input{
				turnId:  -1,
				content: generateValidZipContent(suite.T(), []byte("hello")),
			},
		},
		{
			Name:   "T55-NullBody",
			Output: output{err: api.ErrInvalidParam},
			Input: input{
				turnId:  1,
				content: nil,
			},
		},
		{
			Name:   "T56-Turn not found",
			Output: output{err: api.ErrNotFound},
			Input: input{
				turnId:  1000,
				content: generateValidZipContent(suite.T(), []byte("hello")),
			},
		},
	}
	service := NewRepository(suite.db, suite.testPath)

	for _, tc := range tcs {
		suite.T().Run(tc.Name, func(t *testing.T) {
			suite.SeedTestData()
			defer suite.Cleanup()
			err := service.SaveFile(tc.Input.turnId, tc.Input.content)
			suite.Equalf(
				suite.ErrorIs(err, tc.Output.err), true,
				"exptected %v; got %v", tc.Output.err, err)
		})

	}

}

func (s *RepositorySuite) TeardownSuite() {
	os.RemoveAll(s.testPath)
}

func (suite *RepositorySuite) TestGetFile() {
	type input struct {
		turnId int64
	}

	type output struct {
		fname string
		file  *os.File
		err   error
	}

	tcs := []struct {
		Name   string
		Output output
		Input  input
	}{
		{
			Name:   "T58-TurnNotFound",
			Output: output{fname: "", file: nil, err: api.ErrNotFound},
			Input: input{
				turnId: 100,
			},
		},
		{
			Name:   "T59-BadMetadata",
			Output: output{fname: "", file: nil, err: api.ErrNotFound},
			Input: input{
				turnId: 2,
			},
		},
		{
			Name:   "T510-OkFile",
			Output: output{"1.zip", &os.File{}, nil},
			Input: input{
				turnId: 1,
			},
		},
	}

	for _, tc := range tcs {
		suite.T().Run(tc.Name, func(t *testing.T) {
			suite.SeedTestData()
			defer suite.Cleanup()
			_, f, err := suite.service.GetFile(tc.Input.turnId)
			defer f.Close()
			suite.Equalf(
				suite.ErrorIs(err, tc.Output.err),
				true,
				"%s - exptected %v; got %v", tc.Name, tc.Output.err, err,
			)
		})

	}

}

func TestServiceSuite(t *testing.T) {
	if _, ok := os.LookupEnv("SKIP_INTEGRATION"); ok {
		t.Skip()
	}
	suite.Run(t, new(RepositorySuite))
}
