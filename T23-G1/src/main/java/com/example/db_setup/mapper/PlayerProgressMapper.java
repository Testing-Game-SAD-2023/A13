package com.example.db_setup.mapper;

import com.example.db_setup.model.PlayerProgress;
import com.example.db_setup.model.dto.gamification.PlayerProgressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GameProgressMapper.class)
public interface PlayerProgressMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(source = "progresses", target = "gameProgressesDTO")
    PlayerProgressDTO playerProgressToPlayerProgressDTO(PlayerProgress playerProgress);
}
