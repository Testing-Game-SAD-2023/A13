package com.groom.manvsclass.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import javax.persistence.Id;
import java.time.Instant;

@Document(collection = "opponents")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Opponent {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @Indexed
    private String classUT;

    @Indexed
    private OpponentDifficulty opponentDifficulty;

    @Indexed
    private String opponentType;

    private String coverage;

    private JacocoScore jacocoScore;
    private EvosuiteScore evosuiteScore;

}
