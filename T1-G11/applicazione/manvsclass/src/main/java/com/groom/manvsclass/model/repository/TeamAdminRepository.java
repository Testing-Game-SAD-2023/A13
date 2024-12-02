package com.groom.manvsclass.model.repository;
import com.groom.manvsclass.model.TeamAdmin;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamAdminRepository extends MongoRepository<TeamAdmin, String> {

    // Trova tutte le associazioni per un determinato Admin
    List<TeamAdmin> findByAdminId(String adminId);

    // Trova tutte le associazioni per un determinato Team
    List<TeamAdmin> findByTeamId(String teamId);
}
