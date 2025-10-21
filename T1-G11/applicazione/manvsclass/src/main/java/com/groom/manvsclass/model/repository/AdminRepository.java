package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AdminRepository extends MongoRepository<Admin, String> {
    long count();  // Questo metodo conta automaticamente tutti i documenti nella collezione `Admin`
}
