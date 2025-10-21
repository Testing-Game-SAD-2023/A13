package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
    //MODIFICA 02/12/2024: aggiutna verifica se esiste un team con il nome specificato
    boolean existsByName(String name);


    /**
     * Restituisce il Team in cui è presente l'id dello studente passato come parametro.
     * Se l'id è presente all'interno della lista 'idStudenti' di un team, quel team viene ritornato.
     *
     * @param idStudente l'identificativo dello studente
     * @return il Team associato allo studente oppure null se non trovato
     */
    Team findByIdStudenti(String idStudente);
}
