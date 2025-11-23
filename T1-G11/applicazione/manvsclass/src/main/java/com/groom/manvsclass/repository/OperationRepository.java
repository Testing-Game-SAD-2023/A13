package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}
