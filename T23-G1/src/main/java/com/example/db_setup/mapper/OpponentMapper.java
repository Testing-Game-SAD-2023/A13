package com.example.db_setup.mapper;

import com.example.db_setup.model.Opponent;
import com.example.db_setup.model.dto.gamification.OpponentDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OpponentMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "active"})
    OpponentDTO opponentToOpponentDTO(Opponent opponent);
}
