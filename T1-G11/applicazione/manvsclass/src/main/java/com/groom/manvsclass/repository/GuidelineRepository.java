package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Guideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GuidelineRepository extends JpaRepository<Guideline, Long> {

    boolean existsByTitle(String guidelineTitle);

    Optional<Guideline> findByTitle(String guidelineTitle);
	
	@Query("SELECT g FROM Guideline g WHERE TYPE(g) = Guideline")
	List<Guideline> findAllGuidelines();

}