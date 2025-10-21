package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.Turn;
import com.t4.gamerepo.model.dto.response.TurnDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TurnScoreMapper.class})
public interface TurnMapper {

    TurnDTO turnToTurnDTO(Turn turn);

    List<TurnDTO> turnsToTurnDTOList(List<Turn> turns);
}
