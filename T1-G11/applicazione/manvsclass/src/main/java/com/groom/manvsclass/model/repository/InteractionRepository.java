package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.interaction;

public interface InteractionRepository	extends MongoRepository<interaction,String>{

}
