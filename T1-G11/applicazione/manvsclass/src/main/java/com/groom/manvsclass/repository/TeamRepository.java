package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);

    List<Team> findByAdmin_Email(String adminEmail);

    Optional<Team> findByName(String teamName);

    Optional<Team> findByAdmin_EmailAndName(String adminEmail, String teamName);

    @Query("SELECT t FROM Team t JOIN t.studentIds s WHERE s = :studentId")
    Optional<Team> findByStudentId(@Param("studentId") String studentId);
}
