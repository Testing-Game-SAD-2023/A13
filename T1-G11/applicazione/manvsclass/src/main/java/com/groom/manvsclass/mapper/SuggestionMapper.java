package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.model.Suggestion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface SuggestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classUT", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "image", ignore = true)
    Suggestion toEntity(SuggestionDTO suggestionDTO);

    SuggestionDTO toDto(Suggestion suggestion);

    List<Suggestion> toEntityList(List<SuggestionDTO> suggestionDTOs);

    List<SuggestionDTO> toDtoList(List<Suggestion> suggestions);

}