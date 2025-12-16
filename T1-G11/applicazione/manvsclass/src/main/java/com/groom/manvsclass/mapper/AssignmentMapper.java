package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.AssignmentDTO;
import com.groom.manvsclass.model.Assignment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface AssignmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    @Mapping(target = "team", ignore = true)
    Assignment toEntity(AssignmentDTO assignmentDTO);

    AssignmentDTO toDto(Assignment assignment);

    List<Assignment> toEntityList(List<AssignmentDTO> assignmentDTOs);

    List<AssignmentDTO> toDtoList(List<Assignment> assignments);
}