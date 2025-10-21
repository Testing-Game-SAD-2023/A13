package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.interaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InteractionRepository extends MongoRepository<interaction, String> {

}
