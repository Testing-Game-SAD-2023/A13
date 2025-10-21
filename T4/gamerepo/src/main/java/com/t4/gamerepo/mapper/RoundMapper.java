package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.Round;
import com.t4.gamerepo.model.dto.response.RoundDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TurnMapper.class})
public interface RoundMapper {

    RoundDTO roundToRoundDTO(Round round);

    List<RoundDTO> roundstoRoundDTOList(List<Round> rounds);
}
