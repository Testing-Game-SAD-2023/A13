//Modifica 07/12/2024: Creazione AssignmentRepository

package com.groom.manvsclass.model.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Assignment;

public interface AssignmentRepository	extends MongoRepository<Assignment,String>{

    List<Assignment> findByTeamId(String idTeam);

    List<Assignment> findAllByTeamIdIn(List<String> teamIds);

}
