package com.groom.manvsclass.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import javax.persistence.*; // Importa le annotazioni JPA standard
import java.time.Instant;

@Entity
@Table(name = "opponent")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OpponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_ut_name", referencedColumnName = "name")
    private ClassUTEntity classUt;

    @Enumerated(EnumType.STRING)
    @Column(name = "opponent_difficulty", nullable = false)
    private OpponentDifficulty opponentDifficulty;

    @Column(name = "opponent_type")
    private String opponentType;

    @Lob
    @Column(name = "coverage")
    private String coverage;

    @Type(type = "jsonb") // Annotazione per Hibernate Types
    @Column(name = "jacoco_score", columnDefinition = "jsonb")
    private JacocoScore jacocoScore;

    @Type(type = "jsonb")
    @Column(name = "evosuite_score", columnDefinition = "jsonb")
    private EvosuiteScore evosuiteScore;
}