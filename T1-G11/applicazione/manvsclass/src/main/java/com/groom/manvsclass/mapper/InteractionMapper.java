package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.InteractionDTO;
import com.groom.manvsclass.model.Interaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface InteractionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "classUT", ignore = true)
    Interaction toEntity(InteractionDTO interactionDTO);

    @Mapping(source = "classUT.name", target = "className")
    InteractionDTO toDto(Interaction interaction);

    List<Interaction> toEntityList(List<InteractionDTO> interactionDTOs);

    List<InteractionDTO> toDtoList(List<Interaction> interactions);
}