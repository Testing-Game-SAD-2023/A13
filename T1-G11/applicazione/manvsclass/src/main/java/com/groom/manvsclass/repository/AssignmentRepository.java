package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByTeam_Id(Long teamId);

    List<Assignment> findAllByTeam_IdIn(List<Long> teamIds);

    Optional<Assignment> findByTitle(String assignmentTitle);
}
