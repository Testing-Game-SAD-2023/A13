package com.groom.manvsclass.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.Admin;


public interface AdminRepository	extends MongoRepository<Admin,String>{
	
}
