package com.groom.manvsclass.model.entity;

import javax.persistence.*;

@Entity(name = "achievements")
public class AchievementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private String statistic;

    @Column(name = "progress_required")
    private float progressRequired;
}
