package com.groom.manvsclass.model;

import com.groom.manvsclass.model.ClassUT;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Embedded;
import jakarta.persistence.Column;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "opponents")
public class Opponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String type;

    @Column(columnDefinition="TEXT")
    private String coverage;

    // @Enumerated(EnumType.STRING)
    // private OpponentDifficulty opponentDifficulty;

    @ManyToOne
    @JoinColumn(name = "class_name", referencedColumnName = "name")
    private ClassUT classUT;

    @Embedded
    private JacocoScore jacocoScore;

    @Embedded
    private EvosuiteScore evosuiteScore;
}