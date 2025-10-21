package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.Game;
import com.t4.gamerepo.model.dto.response.GameDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {RoundMapper.class, TurnMapper.class, PlayerResultMapper.class})
public interface GameMapper {

    GameDTO gameToGameDTO(Game game);
}
