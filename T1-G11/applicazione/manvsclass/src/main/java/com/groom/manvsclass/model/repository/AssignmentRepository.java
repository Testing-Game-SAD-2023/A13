//Modifica 07/12/2024: Creazione AssignmentRepository

package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Assignment;

public interface AssignmentRepository	extends MongoRepository<Assignment,String>{

}
