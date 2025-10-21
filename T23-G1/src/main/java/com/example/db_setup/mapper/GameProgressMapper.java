package com.example.db_setup.mapper;

import com.example.db_setup.model.GameProgress;
import com.example.db_setup.model.dto.gamification.GameProgressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameProgressMapper {

    @Mapping(source = "winner", target = "won")
    @Mapping(source = "gameProgress.opponent.gameMode", target = "gameMode")
    @Mapping(source = "gameProgress.opponent.classUT", target = "classUT")
    @Mapping(source = "gameProgress.opponent.type", target = "type")
    @Mapping(source = "gameProgress.opponent.difficulty", target = "difficulty")
    GameProgressDTO gameProgressToGameProgressDTO(GameProgress gameProgress);

    List<GameProgressDTO> gameProgressToGameProgressDTOList(List<GameProgress> gameProgresses);
}
