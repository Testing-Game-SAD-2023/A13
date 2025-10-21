package com.t4.gamerepo.model.dto.response;

import com.t4.gamerepo.model.dto.common.TurnScoreDTO;

import java.sql.Timestamp;

public record TurnDTO(
        Long playerId,
        int turnNumber,
        TurnScoreDTO score,
        Timestamp startedAt,
        Timestamp closedAt
) {
}
