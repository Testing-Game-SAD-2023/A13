package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.ScalataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScalataRepository extends JpaRepository<ScalataEntity, String> {

    List<ScalataEntity> findByUsernameContainingIgnoreCase(String username);

    List<ScalataEntity> findByScalataNameContainingIgnoreCase(String scalataName);

    List<ScalataEntity> findByNumberOfRounds(int numberOfRounds);
}
