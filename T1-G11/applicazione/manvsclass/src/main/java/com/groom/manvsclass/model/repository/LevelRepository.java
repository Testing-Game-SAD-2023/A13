package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Level;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/*
 * LevelRepository extends MongoRepository with:
 * 'Level' as the domain type and
 * 'Integer' as the type of the id field (idLevel)
 *
 * LevelRepository inherits several methods such as:
 * save(), findById(), findAll(), count(), delete(), ... and
 * this allows to perform CRUD operations on the 'Level' objects
 */
public interface LevelRepository extends MongoRepository<Level, Integer> {

    // Returns all Level objects associated with a given Scalata
    List<Level> findByScalataName(String scalataName);

}
