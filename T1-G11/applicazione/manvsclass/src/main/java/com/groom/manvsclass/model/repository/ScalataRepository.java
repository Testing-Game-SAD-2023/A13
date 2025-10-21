package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Scalata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/*
 * ScalataRepository extends MongoRepository with:
 * 'Scalata' as the domain type and
 * 'String' as the type of the id field
 *
 * ScalataRepository inherits several methods such as:
 * save(), findOne(), findAll(), count(), delete(), ... and
 * this allow to perform CRUD operation on the 'Scalata' objects
 */
public interface ScalataRepository extends MongoRepository<Scalata, String> {

    // Returns all the Scalata objects with the given author
    List<Scalata> findByUsernameContaining(String username);

    // Returns all the Scalata objects with the given rounds
    List<Scalata> findByNumberOfRoundsContaining(int numberOfRounds);

    // Returns all the Scalata objects with the given name
    List<Scalata> findByScalataNameContaining(String scalataName);

}
