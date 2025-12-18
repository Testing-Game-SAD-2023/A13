package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.InteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<InteractionEntity, Integer> {

    List<InteractionEntity> findByInteractionType(Integer type);

    @Query("SELECT COUNT(i) FROM InteractionEntity i WHERE i.classUt.name = :className AND i.interactionType = 1")
    long countLikesByClassName(@Param("className") String className);
}
