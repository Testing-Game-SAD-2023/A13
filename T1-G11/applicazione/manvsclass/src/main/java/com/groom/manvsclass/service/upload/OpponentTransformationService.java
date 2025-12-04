package com.groom.manvsclass.service.upload;

import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.service.upload.score.CoverageReader;
import com.groom.manvsclass.service.upload.score.ScoreBuilder;
import com.groom.manvsclass.util.upload.LevelExtractor;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class OpponentTransformationService {

    private final OpponentPersistenceService opponentPersistenceService;

    public OpponentTransformationService(OpponentPersistenceService opponentPersistenceService) {
        this.opponentPersistenceService = opponentPersistenceService;
    }

    public void createAndPersistOpponent(String classUTName, String robotType, File levelFolder, 
                                         Path evosuitePath, Path jacocoPath) throws IOException {
        
        try {
            CoverageReader.CoverageData coverageData = CoverageReader.readCoverageFiles(evosuitePath, jacocoPath);
            
            OpponentDifficulty difficulty = LevelExtractor.extractDifficulty(levelFolder);
            JacocoScore jacocoScore = ScoreBuilder.buildJacocoScore(coverageData.jacocoStatistics);
            EvosuiteScore evosuiteScore = ScoreBuilder.buildEvosuiteScore(coverageData.evosuiteStatistics);

            Opponent opponent = new Opponent();
            opponent.setClassUT(classUTName);
            opponent.setOpponentType(robotType);
            opponent.setOpponentDifficulty(difficulty);
            opponent.setCoverage(coverageData.jacococCoverageXml);
            opponent.setEvosuiteScore(evosuiteScore);
            opponent.setJacocoScore(jacocoScore);

            opponentPersistenceService.saveOpponentAndNotify(opponent);
        } catch (IllegalArgumentException e) {
            throw new com.groom.manvsclass.service.exception.RobotProcessingException(
                "Errore durante l'elaborazione del livello '" + levelFolder.getName() + "': " + e.getMessage(),
                e
            );
        } catch (IOException e) {
            throw new IOException(
                "Errore durante la lettura dei file di coverage per il livello '" + levelFolder.getName() + "': " + e.getMessage(),
                e
            );
        }
    }
}
