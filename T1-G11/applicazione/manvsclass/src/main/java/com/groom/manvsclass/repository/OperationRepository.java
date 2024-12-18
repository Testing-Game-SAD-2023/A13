package com.groom.manvsclass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Operation;

public interface OperationRepository extends MongoRepository<Operation, String> {

}
