package turn

import (
	"archive/zip"
	"database/sql"
	"errors"
	"fmt"
	"io"
	"os"
	"path"
	"path/filepath"
	"strconv"
	"time"

	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"
)

type Repository struct {
	db      *gorm.DB
	dataDir string
}

func NewRepository(db *gorm.DB, dataDir string) *Repository {
	return &Repository{
		db:      db,
		dataDir: dataDir,
	}
}

func (tr *Repository) CreateBulk(r *CreateRequest) ([]Turn, error) {
	turns := make([]model.Turn, len(r.Players))

	err := tr.db.Transaction(func(tx *gorm.DB) error {
		var (
			err error
		)

		err = tx.Where(&model.Round{ID: r.RoundId}).
			First(&model.Round{}).
			Error
		if err != nil {
			return err
		}

		var ids []int64
		err = tx.
			Model(&model.Player{}).
			Select("id").
			Where("account_id in ?", r.Players).
			Find(&ids).
			Error

		if err != nil {
			return err
		}

		if len(ids) != len(r.Players) && !api.Duplicated(r.Players) {
			return fmt.Errorf("%w: invalid player list", api.ErrInvalidParam)
		}

		for i, id := range ids {
			turns[i] = model.Turn{
				PlayerID:  id,
				RoundID:   r.RoundId,
				StartedAt: r.StartedAt,
				ClosedAt:  r.ClosedAt,
			}
		}

		return tx.Create(&turns).Error
	})
	resp := make([]Turn, len(turns))
	for i, turn := range turns {
		resp[i] = fromModel(&turn)
	}

	return resp, api.MakeServiceError(err)
}

func (tr *Repository) Update(id int64, r *UpdateRequest) (Turn, error) {

	var (
		turn model.Turn = model.Turn{ID: id}
		err  error
	)

	err = tr.db.Model(&turn).Updates(r).Error

	return fromModel(&turn), api.MakeServiceError(err)
}

func (tr *Repository) FindById(id int64) (Turn, error) {
	var turn model.Turn

	err := tr.db.
		First(&turn, id).
		Error

	return fromModel(&turn), api.MakeServiceError(err)
}

func (tr *Repository) FindByRound(id int64) ([]Turn, error) {
	var turns []model.Turn

	err := tr.db.
		Where(&model.Turn{RoundID: id}).
		Find(&turns).
		Error
	resp := make([]Turn, len(turns))
	for i, turn := range turns {
		resp[i] = fromModel(&turn)
	}
	return resp, api.MakeServiceError(err)
}

func (tr *Repository) Delete(id int64) error {

	db := tr.db.
		Where(&model.Turn{ID: id}).
		Delete(&model.Turn{})

	if db.Error != nil {
		return db.Error
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}

	return nil

}

func (ts *Repository) SaveFile(id int64, r io.Reader) error {
	if r == nil {
		return fmt.Errorf("%w: body is empty", api.ErrInvalidParam)
	}
	err := ts.db.Transaction(func(tx *gorm.DB) error {
		var (
			err   error
			round model.Round
		)

		err = tx.
			Joins("join turns on turns.round_id = rounds.id where turns.id  = ?", id).
			First(&round).
			Error

		if err != nil {
			return err
		}

		dst, err := os.CreateTemp("", "")
		if err != nil {
			return err
		}
		defer os.Remove(dst.Name())
		if _, err := io.Copy(dst, r); err != nil {
			return err
		}

		if zfile, err := zip.OpenReader(dst.Name()); err != nil {
			return api.ErrNotAZip
		} else {
			zfile.Close()
		}

		year := time.Now().Year()

		fname := path.Join(ts.dataDir,
			strconv.FormatInt(int64(year), 10),
			strconv.FormatInt(round.GameID, 10),
			fmt.Sprintf("%d.zip", id),
		)

		dir := path.Dir(fname)
		if err := os.MkdirAll(dir, os.ModePerm); err != nil && !errors.Is(err, os.ErrExist) {
			return err
		}

		if err := os.Rename(dst.Name(), fname); err != nil {
			return err
		}

		return tx.FirstOrCreate(
			&model.Metadata{
				TurnID: sql.NullInt64{Int64: id, Valid: true},
				Path:   fname,
			}).
			Error

	})

	return api.MakeServiceError(err)

}

func (ts *Repository) GetFile(id int64) (string, *os.File, error) {
	var (
		metadata model.Metadata
		err      error
	)

	err = ts.db.
		Where(&model.Metadata{TurnID: sql.NullInt64{Int64: id, Valid: true}}).
		First(&metadata).
		Error

	if err != nil {
		return "", nil, api.MakeServiceError(err)
	}

	f, err := os.Open(metadata.Path)

	if errors.Is(err, os.ErrNotExist) {
		return "", nil, api.ErrNotFound
	} else if err != nil {
		return "", nil, err
	}

	return filepath.Base(metadata.Path), f, nil
}
