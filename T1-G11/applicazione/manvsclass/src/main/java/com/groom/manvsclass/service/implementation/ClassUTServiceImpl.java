package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.entity.OperationEntity;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.ClassUTRepository;
import com.groom.manvsclass.model.repository.OperationRepository;
import com.groom.manvsclass.service.ClassUTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ClassUTServiceImpl implements ClassUTService {

    @Autowired
    private ClassUTRepository classUTRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OperationRepository operationRepository;


    @Override
    @Transactional
    public Long updateClassUT(String oldName, ClassUTEntity newContent) {

        // 1. Cerchi l'entità esistente usando il vecchio nome (ID attuale)
        Optional<ClassUTEntity> existingOpt = classUTRepository.findById(oldName);

        if (existingOpt.isPresent()) {
            ClassUTEntity existing = existingOpt.get();

            existing.setDate(newContent.getDate());
            existing.setDifficulty(newContent.getDifficulty());
            existing.setDescription(newContent.getDescription());
            existing.setCategory(newContent.getCategory());
            existing.setUri(newContent.getUri());

            classUTRepository.save(existing);

            return 1L; // 1 record modificato
        }

        return 0L; // Nessun record trovato
    }

    @Override
    @Transactional
    public ClassUTEntity deleteClassUT(String name) {

        // 1. Cerchiamo l'entità prima di cancellarla (sostituisce la Query criteria)
        ClassUTEntity classToDelete = classUTRepository.findById(name)
                .orElseThrow(() -> new RuntimeException("Classe non trovata: " + name));

        // 2. Esegui la tua logica custom per i file
//        eliminaFile(name); //DA FARE

        // 3. Preparazione dati per l'operazione
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);

        AdminEntity adminEntity = adminRepository.findByUsername("userAdmin");
        // Gestire il caso in cui admin è null, se necessario

        // 4. Creazione OperationEntity
        // NOTA: In JPA non dovresti passare l'ID manualmente (count).
        // Lascia che sia il DB a generarlo (passa null o usa un costruttore senza ID).
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAdmin(adminEntity);
        operationEntity.setClassName(classToDelete.getName());
        operationEntity.setType(2);
        operationEntity.setDate(data);

        // Se la tua entity ha ancora il costruttore manuale:
        // OperationEntity op = new OperationEntity(null, adminEntity, classToDelete, 2, data);

        operationRepository.save(operationEntity);

        // 5. Cancellazione (sostituisce findAndRemove)
        classUTRepository.delete(classToDelete);

        // 6. Restituisce l'oggetto che era in memoria (Mongo lo restituiva alla fine)
        return classToDelete;
    }
}
