package com.t4.gamerepo.model.dto.response;

import com.t4.gamerepo.model.GameStatus;
import com.t4.gamerepo.model.dto.common.PlayerResultDTO;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public record GameDTO(
        Long id,
        List<Long> players,
        GameStatus status,
        GameMode gameMode,
        Map<Long, PlayerResultDTO> playerResults,
        List<RoundDTO> rounds,
        Timestamp startedAt,
        Timestamp closedAt
) {
}
