package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.ClassUT;

public interface ClassRepository	extends MongoRepository<ClassUT,String>{

}
