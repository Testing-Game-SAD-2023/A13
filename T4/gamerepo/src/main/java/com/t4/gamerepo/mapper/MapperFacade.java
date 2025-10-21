package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.*;
import com.t4.gamerepo.model.dto.common.PlayerResultDTO;
import com.t4.gamerepo.model.dto.common.TurnScoreDTO;
import com.t4.gamerepo.model.dto.response.GameDTO;
import com.t4.gamerepo.model.dto.response.RoundDTO;
import com.t4.gamerepo.model.dto.response.TurnDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MapperFacade {
    private final GameMapper gameMapper;
    private final RoundMapper roundMapper;
    private final TurnMapper turnMapper;
    private final TurnScoreMapper turnScoreMapper;
    private final PlayerResultMapper playerResultMapper;

    public GameDTO toDTO(Game game) {
        return gameMapper.gameToGameDTO(game);
    }

    public RoundDTO toDTO(Round round) {
        return roundMapper.roundToRoundDTO(round);
    }

    public TurnDTO toDTO(Turn turn) {
        return turnMapper.turnToTurnDTO(turn);
    }

    public TurnScore toEntity(TurnScoreDTO dto) {
        return turnScoreMapper.turnScoreDTOToTurnScore(dto);
    }

    public PlayerResult toEntity(PlayerResultDTO dto) {
        return playerResultMapper.playerResultDTOToPlayerResult(dto);
    }


}
