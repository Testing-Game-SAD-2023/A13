package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    // Sostituisce findReport() (da SearchRepositoryImpl)
    // Assumendo che "type 0" sia per i report
    List<Interaction> findByType(int type);

    // Sostituisce getLikes(String name) (da SearchRepositoryImpl)
    // Assumendo che "type 1" sia per i like e che "name" sia il nome della ClassUT
    long countByClassUT_NameAndType(String classUTName, int type);
}