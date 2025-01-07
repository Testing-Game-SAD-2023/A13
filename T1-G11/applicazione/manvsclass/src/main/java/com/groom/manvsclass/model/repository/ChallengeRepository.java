package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Challenge;

public interface ChallengeRepository extends MongoRepository<Challenge,String>{
    
} 

