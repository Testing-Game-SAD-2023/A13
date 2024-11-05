package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Achievement;
import com.groom.manvsclass.model.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StatisticRepository extends MongoRepository<Statistic, String> {
    List<Statistic> findByNameContaining(String name);
}
