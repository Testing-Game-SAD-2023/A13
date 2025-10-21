package com.t4.gamerepo.mapper;

import com.t4.gamerepo.model.TurnScore;
import com.t4.gamerepo.model.dto.common.TurnScoreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import testrobotchallenge.commons.mappers.CoverageMapper;
import testrobotchallenge.commons.mappers.EvosuiteScoreMapper;
import testrobotchallenge.commons.mappers.JacocoScoreMapper;

@Mapper(componentModel = "spring", uses = {JacocoScoreMapper.class, EvosuiteScoreMapper.class, CoverageMapper.class})
public interface TurnScoreMapper {

    @Mapping(source = "evosuiteScore", target = "evosuiteScoreDTO", qualifiedByName = "evosuiteScoreToEvosuiteScoreDTO")
    @Mapping(source = "jacocoScore", target = "jacocoScoreDTO", qualifiedByName = "jacocoScoreToJacocoScoreDTO")
    TurnScoreDTO turnScoreToTurnScoreDTO(TurnScore turnScore);

    @Mapping(source = "evosuiteScoreDTO", target = "evosuiteScore", qualifiedByName = "evosuiteScoreDTOToEvosuiteScore")
    @Mapping(source = "jacocoScoreDTO", target = "jacocoScore", qualifiedByName = "jacocoScoreDTOToJacocoScore")
    TurnScore turnScoreDTOToTurnScore(TurnScoreDTO turnScoreDTO);
}
