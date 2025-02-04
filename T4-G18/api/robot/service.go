package robot

import (
	"fmt"
	"math/rand"
	"github.com/alarmfox/game-repository/api"
	"github.com/alarmfox/game-repository/model"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

type RobotStorage struct {
	db *gorm.DB
}

func NewRobotStorage(db *gorm.DB) *RobotStorage {
	return &RobotStorage{
		db: db,
	}
}

func (rs *RobotStorage) CreateBulk(r *CreateRequest) (int, error) {
	robots := make([]model.Robot, len(r.Robots))

	// Prepara i robot da creare
	for i, robot := range r.Robots {
		robots[i] = model.Robot{
			TestClassId: robot.TestClassId,
			Scores:      robot.Scores,
			Difficulty:  robot.Difficulty,
			Type:        robot.Type.AsInt8(),
		}
	}

	// Crea i robot in batch, aggiornando in caso di conflitti
	err := rs.db.
		Clauses(clause.OnConflict{
			UpdateAll: true,
		}).
		CreateInBatches(&robots, 100).
		Error

	return len(robots), api.MakeServiceError(err)
}

func (gs *RobotStorage) FindByFilter(testClassId string, difficulty string, t RobotType) (Robot, error) {
	var robot model.Robot
	var ids []int64

	// Avvia una transazione per garantire l'integrità dei dati
	err := gs.db.Transaction(func(tx *gorm.DB) error {
		// Trova gli ID dei robot che corrispondono ai filtri
		err := tx.
			Model(&model.Robot{}).
			Select("id").
			Where(&model.Robot{
				TestClassId: testClassId,
				Difficulty:  difficulty,
			}).
			Where("type = ?", t.AsInt8()).
			Find(&ids).
			Error

		if err != nil {
			return err
		}

		// Verifica se ci sono robot trovati
		if len(ids) == 0 {
			return gorm.ErrRecordNotFound
		}

		// Seleziona un ID in base al tipo di robot
		var id int64
		switch t {
		case evosuite:
			id = ids[0] // Prendi il primo robot
		case randoop:
			pos := rand.Intn(len(ids)) // Se il tipo è randoop, seleziona uno random
			id = ids[pos]
		default:
			return fmt.Errorf("%w: unsupported test engine", api.ErrInvalidParam)
		}

		// Carica il robot con i round associati
		return tx.Preload("Rounds").First(&robot, id).Error
	})

	// Restituisci il robot convertito nel formato API
	return *fromModel(&robot), api.MakeServiceError(err)
}

//nuova delete considera la relazione gerarchica tra i dati (round dipende da robot)
func (rs *RobotStorage) DeleteByTestClass(testClassId string) error {
	// Avvia una transazione per gestire l'eliminazione
	err := rs.db.Transaction(func(tx *gorm.DB) error {
		// Elimina i round associati ai robot con il TestClassId specificato
		if err := tx.Where("robot_id IN (SELECT id FROM robots WHERE test_class_id = ?)", testClassId).
			Delete(&model.Round{}).Error; err != nil {
			return err
		}

		// Elimina i robot con il TestClassId specificato
		if err := tx.Where(&model.Robot{TestClassId: testClassId}).Delete(&model.Robot{}).Error; err != nil {
			return err
		}

		return nil
	})

	return api.MakeServiceError(err)
}

// FindByID recupera un robot per il suo ID, pre-caricando i round associati.
func (rs *RobotStorage) FindByID(robotID int64) (Robot, error) {
	var robot model.Robot

	err := rs.db.
		Preload("Rounds"). // Carica i round associati
		First(&robot, robotID).
		Error

	return *fromModel(&robot), api.MakeServiceError(err)
}

// Update aggiorna le informazioni di un robot.
func (rs *RobotStorage) Update(robotID int64, r *UpdateRequest) (Robot, error) {
	var robot model.Robot

	// Trova il robot esistente
	err := rs.db.First(&robot, robotID).Error
	if err != nil {
		return Robot{}, api.MakeServiceError(err)
	}

	// Aggiorna i campi specificati
	err = rs.db.Model(&robot).Updates(r).Error
	if err != nil {
		return Robot{}, api.MakeServiceError(err)
	}

	// Restituisci il robot aggiornato
	return *fromModel(&robot), nil
}