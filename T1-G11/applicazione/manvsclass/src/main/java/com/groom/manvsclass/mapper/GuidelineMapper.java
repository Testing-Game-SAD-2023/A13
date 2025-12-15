package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.model.Guideline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface GuidelineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "image", ignore = true)
    Guideline toEntity(GuidelineDTO guidelineDTO);

    GuidelineDTO toDto(Guideline guideline);

    List<Guideline> toEntityList(List<GuidelineDTO> guidelineDTOs);

    List<GuidelineDTO> toDtoList(List<Guideline> guidelines);

}