package main

import (
	"database/sql"
	"log"
	"os"
	"testing"

	"github.com/alarmfox/game-repository/model"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func TestCleanup(t *testing.T) {
	if _, ok := os.LookupEnv("SKIP_INTEGRATION"); ok {
		t.Skip()
	}

	postgresAddr := os.Getenv("DB_URI")
	db, err := gorm.Open(postgres.Open(postgresAddr), &gorm.Config{
		SkipDefaultTransaction: true,
	})
	if err != nil {
		log.Fatal(err)
	}
	err = db.AutoMigrate(
		&model.Game{},
		&model.Round{},
		&model.Player{},
		&model.Turn{},
		&model.Metadata{},
		&model.PlayerGame{},
		&model.Robot{})

	if err != nil {
		t.Fatal(err)
	}
	seed(t, db)

	n, err := cleanup(db)

	if err != nil {
		t.Fatal(err)
	}

	if n != 2 {
		t.Fatalf("expected n=2; got n=%d", n)
	}

}

func seed(t *testing.T, db *gorm.DB) {
	t.Helper()

	tmpDir := os.TempDir()
	f1, err := os.CreateTemp(tmpDir, "")
	if err != nil {
		t.Fatal(err)
	}
	defer f1.Close()
	f1.Write([]byte("jfj"))

	f2, err := os.CreateTemp(tmpDir, "")
	if err != nil {
		t.Fatal(err)
	}
	defer f2.Close()

	f2.Write([]byte("jfj"))

	toInsert := []model.Metadata{
		{
			Path:   f1.Name(),
			TurnID: sql.NullInt64{Valid: false},
		},
		{
			Path:   f2.Name(),
			TurnID: sql.NullInt64{Valid: false},
		},
	}

	if err := db.Create(&toInsert).Error; err != nil {
		t.Fatal(err)
	}

	t.Cleanup(func() {
		err := db.
			Session(&gorm.Session{AllowGlobalUpdate: true}).
			Delete(&model.Metadata{}).
			Error

		if err != nil {
			t.Fatal(err)
		}

		os.Remove(f1.Name())
		os.Remove(f2.Name())
	})
}
