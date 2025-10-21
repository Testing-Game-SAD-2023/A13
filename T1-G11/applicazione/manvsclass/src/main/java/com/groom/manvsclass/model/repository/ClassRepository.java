package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.ClassUT;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassRepository extends MongoRepository<ClassUT, String> {

}
