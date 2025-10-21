package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.PlayerResult;
import com.t4.gamerepo.model.dto.common.PlayerResultDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerResultMapper {

    PlayerResultDTO playerResultToPlayerResultDTO(PlayerResult playerResult);

    PlayerResult playerResultDTOToPlayerResult(PlayerResultDTO playerResultDTO);

}
