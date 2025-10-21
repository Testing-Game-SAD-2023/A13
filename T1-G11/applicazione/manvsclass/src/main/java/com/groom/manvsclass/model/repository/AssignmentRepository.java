//Modifica 07/12/2024: Creazione AssignmentRepository

package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    List<Assignment> findByTeamId(String idTeam);

    List<Assignment> findAllByTeamIdIn(List<String> teamIds);

}
