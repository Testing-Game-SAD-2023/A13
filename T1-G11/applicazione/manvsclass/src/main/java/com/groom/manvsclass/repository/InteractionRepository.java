package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {


    List<Interaction> findByType(InteractionType type);

    long countByClassUT_NameAndType(String className, InteractionType type);
}