package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    List<Suggestion> findAllByClassUT_Name(String className);

    Optional<Suggestion> findByClassUT_NameAndTitle(String className, String suggestionTitle);

    boolean existsByClassUT_NameAndTitle(String className, String suggestionTitle);

}