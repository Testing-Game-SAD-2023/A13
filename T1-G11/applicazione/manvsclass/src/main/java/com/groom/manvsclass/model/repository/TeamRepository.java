package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Team;

public interface TeamRepository extends MongoRepository<Team, String> {
    //MODIFICA 02/12/2024: aggiutna verifica se esiste un team con il nome specificato
    boolean existsByName(String name);
    
}
