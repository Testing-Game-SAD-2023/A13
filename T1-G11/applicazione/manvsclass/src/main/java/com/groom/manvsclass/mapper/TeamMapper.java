package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.TeamDTO;
import com.groom.manvsclass.model.Team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface TeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    @Mapping(target = "admin", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    TeamDTO toDto(Team team);

    List<Team> toEntityList(List<TeamDTO> teamDTOs);

    List<TeamDTO> toDtoList(List<Team> teams);
}