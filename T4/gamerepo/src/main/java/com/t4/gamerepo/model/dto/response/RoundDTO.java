package com.t4.gamerepo.model.dto.response;

import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.sql.Timestamp;
import java.util.List;

public record RoundDTO(
        String classUT,
        String type,
        OpponentDifficulty difficulty,
        int roundNumber,
        List<TurnDTO> turns,
        Timestamp startedAt,
        Timestamp closedAt
) {
}
