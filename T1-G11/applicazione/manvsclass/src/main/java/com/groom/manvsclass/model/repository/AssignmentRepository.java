package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {

    List<AssignmentEntity> findByTeam_IdTeam(String idTeam);

    List<AssignmentEntity> findByTeam_IdTeamIn(List<String> teamIds);
}
