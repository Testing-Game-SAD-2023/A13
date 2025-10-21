package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Opponent;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.util.List;
import java.util.Optional;

@Repository
public class OpponentRepositoryImpl implements OpponentRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public OpponentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Opponent> findAllOpponents() {
        return mongoTemplate.findAll(Opponent.class, "opponents");
    }

    @Override
    public void saveOpponent(Opponent opponent) {
        mongoTemplate.save(opponent, "opponents");
    }

    @Override
    public Optional<Opponent> findOpponent(String classUT, String type, OpponentDifficulty difficulty) {
        Query query = buildQuery(classUT, type, difficulty);
        Opponent result = mongoTemplate.findOne(query, Opponent.class, "opponents");
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<EvosuiteScore> findEvosuiteScore(String classUT, String type, OpponentDifficulty difficulty) {
        Query query = buildQuery(classUT, type, difficulty);
        query.fields().include("evosuiteScore").exclude("_id");
        Document doc = mongoTemplate.findOne(query, Document.class, "opponents");
        return Optional.ofNullable(doc != null ? mongoTemplate.getConverter()
                .read(EvosuiteScore.class, (Document) doc.get("evosuiteScore")) : null);
    }

    @Override
    public Optional<JacocoScore> findJacocoScore(String classUT, String type, OpponentDifficulty difficulty) {
        Query query = buildQuery(classUT, type, difficulty);
        query.fields().include("jacocoScore").exclude("_id");
        Document doc = mongoTemplate.findOne(query, Document.class, "opponents");
        return Optional.ofNullable(doc != null ? mongoTemplate.getConverter()
                .read(JacocoScore.class, (Document) doc.get("jacocoScore")) : null);
    }

    @Override
    public Optional<String> findCoverage(String classUT, String type, OpponentDifficulty difficulty) {
        Query query = buildQuery(classUT, type, difficulty);
        query.fields().include("coverage").exclude("_id");
        Document doc = mongoTemplate.findOne(query, Document.class, "opponents");
        return Optional.ofNullable(doc != null ? doc.getString("coverage") : null);
    }

    private Query buildQuery(String classUT, String type, OpponentDifficulty difficulty) {
        return new Query(
                Criteria.where("classUT").is(classUT)
                        .and("opponentType").is(type)
                        .and("opponentDifficulty").is(difficulty)
        );
    }
}
