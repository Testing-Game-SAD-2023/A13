package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Scalata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScalataRepository extends JpaRepository<Scalata, String> {

    List<Scalata> findByAdmin_Username(String adminUsername);

    List<Scalata> findByAdmin_Email(String adminEmail);

    List<Scalata> findByNumLevels(int numLevels);

}
