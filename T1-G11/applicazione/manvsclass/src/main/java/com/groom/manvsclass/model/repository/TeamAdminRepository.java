package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.TeamAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamAdminRepository extends JpaRepository<TeamAdminEntity, String> {

    Optional<TeamAdminEntity> findByAdmin_Email(String email);

    Optional<TeamAdminEntity> findByTeam_IdTeam(String teamId);

    List<TeamAdminEntity> findAllByAdmin_Email(String email);

    boolean existsByAdmin_EmailAndTeam_IdTeam(String email, String teamId);
}
