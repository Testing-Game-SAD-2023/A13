package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Integer> {

    void deleteByClassName(String className);
}
