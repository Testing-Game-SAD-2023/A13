package com.groom.manvsclass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.groom.manvsclass.model.AuthenticatedAdmin;

@Repository
public interface AuthenticatedAdminRepository extends MongoRepository<AuthenticatedAdmin, String> {

    AuthenticatedAdmin findByAuthToken(String authToken);
}
