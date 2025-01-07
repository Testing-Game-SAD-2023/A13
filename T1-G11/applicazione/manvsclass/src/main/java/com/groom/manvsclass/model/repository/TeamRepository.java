package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Team;

public interface TeamRepository extends MongoRepository<Team,String>{
    boolean existsByTeamName(String teamName);
} 
