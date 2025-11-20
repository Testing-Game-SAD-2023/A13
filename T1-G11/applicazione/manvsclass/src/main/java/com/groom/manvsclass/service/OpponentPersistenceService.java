package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.repository.OpponentRepository;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.GameMode;

@Service
public class OpponentPersistenceService {

    private final OpponentRepository opponentRepository;
    private final ApiGatewayClient apiGatewayClient;

    public OpponentPersistenceService(OpponentRepository opponentRepository, ApiGatewayClient apiGatewayClient) {
        this.opponentRepository = opponentRepository;
        this.apiGatewayClient = apiGatewayClient;
    }

    public void saveOpponentAndNotify(Opponent opponent) {
        opponentRepository.saveOpponent(opponent);

        for (GameMode mode : GameMode.values()) {
            apiGatewayClient.callAddNewOpponent(opponent.getClassUT(), mode, opponent.getOpponentType(), opponent.getOpponentDifficulty());
        }
    }
}
