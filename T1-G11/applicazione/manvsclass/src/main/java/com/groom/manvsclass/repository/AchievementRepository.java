package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AchievementRepository extends MongoRepository<Achievement, String> {
    List<Achievement> findByNameContaining(String name);

    List<Achievement> findByDescriptionContaining(String description);

    List<Achievement> findByStatistic(int statistic);
}
