package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Operation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperationRepository extends MongoRepository<Operation, String> {

}
