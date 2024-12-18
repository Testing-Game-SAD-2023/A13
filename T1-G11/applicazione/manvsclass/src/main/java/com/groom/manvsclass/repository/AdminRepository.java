package com.groom.manvsclass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Admin;

public interface AdminRepository extends MongoRepository<Admin, String> {
    long count(); // Questo metodo conta automaticamente tutti i documenti nella collezione
                  // `Admin`
}
