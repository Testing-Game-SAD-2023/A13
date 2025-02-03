package round

import (
	"errors"
	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{
		db: db,
	}
}

func (rs *Repository) Create(r *CreateRequest) (Round, error) {
	var round model.Round

	err := rs.db.Transaction(func(tx *gorm.DB) error {

		var lastRound model.Round
		err := tx.Where(&model.Round{GameID: r.GameId}).
			Order("\"order\" desc").
			Last(&lastRound).
			Error

		if err != nil && !errors.Is(err, gorm.ErrRecordNotFound) {
			return err
		}

		round = model.Round{
			GameID:      r.GameId,
			TestClassId: r.TestClassId,
			StartedAt:   r.StartedAt,
			ClosedAt:    r.ClosedAt,
			RobotID:     r.RobotID, // Assegniamo un solo robot al round
			Order:       lastRound.Order + 1,
		}

		return tx.Create(&round).Error

	})

	return fromModel(&round), api.MakeServiceError(err)
}

func (r *Repository) Update(id int64, req *UpdateRequest) (Round, error) {
	var round model.Round
	err := r.db.Model(&round).Where("id = ?", id).Updates(req).Error
	if err != nil {
		return Round{}, api.MakeServiceError(err)
	}
	return fromModel(&round), api.MakeServiceError(err)
}

func (r *Repository) FindById(id int64) (Round, error) {
	var round model.Round
	err := r.db.Preload("Robot").First(&round, id).Error // Carica anche il robot associato
	return fromModel(&round), api.MakeServiceError(err)
}

func (rs *Repository) FindByGame(gameID int64) ([]Round, error) {
    var rounds []model.Round

    // Esegui la query per trovare i round associati al GameID specificato
    err := rs.db.
        Where(&model.Round{GameID: gameID}). // Filtro per il GameID
        Preload("Robot").                   // Carica il Robot associato al Round
        Order("\"order\" asc").             // Ordina per ordine crescente
        Find(&rounds).                      // Esegui la query e riempi il risultato
        Error

    if err != nil {
        return nil, api.MakeServiceError(err) // Gestione dell'errore
    }

    // Converte i round dal modello database al formato API
    resp := make([]Round, len(rounds))
    for i, round := range rounds {
        resp[i] = fromModel(&round) // Converte il modello database in formato API
    }

    return resp, nil
}

//potrebbe anche bastare questa versione compatta
/*func (r *Repository) Delete(id int64) error {
	db := r.db.Where(&model.Round{ID: id}).Delete(&model.Round{})
	if db.Error != nil {
		return api.MakeServiceError(db.Error)
	} else if db.RowsAffected < 1 {
		return api.ErrNotFound
	}
	return nil
}*/

func (rs *Repository) Delete(id int64) error {
	err := rs.db.Transaction(func(tx *gorm.DB) error {
		var round model.Round
		db := rs.db.
			Where(&model.Round{ID: id}).
			Clauses(clause.Returning{}).
			Delete(&round)

		if db.Error != nil {
			return db.Error
		} else if db.RowsAffected < 1 {
			return api.ErrNotFound
		}

		err := rs.db.
			Model(&model.Round{}).
			Where(&model.Round{GameID: round.GameID}).
			Where("\"order\" > ?", round.Order).
			UpdateColumn("order", gorm.Expr("\"order\" - ?", 1)).
			Error

		return err
	})

	return api.MakeServiceError(err)
}
