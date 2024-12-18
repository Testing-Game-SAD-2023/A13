package com.groom.manvsclass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.ClassUT;

// Presenta alcuni metodi predefiniti per l'interazione con MongoDB
public interface ClassRepository extends MongoRepository<ClassUT, String> {

}
