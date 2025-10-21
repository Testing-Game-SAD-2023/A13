package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.TeamAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamAdminRepository extends MongoRepository<TeamAdmin, String> {

    // Trova tutte le associazioni per un determinato Admin
    TeamAdmin findByAdminId(String adminId);

    // Trova tutte l'associazione relativa ad un determinato Team
    TeamAdmin findByTeamId(String teamId);

    // Trova tutte le associazioni per un determinato Admin (questa è una versione più generica)
    List<TeamAdmin> findAllByAdminId(String adminId);  // metodo che restituisce tutte le associazioni per un admin
}
