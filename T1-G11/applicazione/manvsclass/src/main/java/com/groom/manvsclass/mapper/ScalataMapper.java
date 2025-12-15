package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.ScalataDTO;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.dto.ClassUTScalataDTO;
import com.groom.manvsclass.model.ClassUTScalata;
import com.groom.manvsclass.model.ClassUTScalataId;
import com.groom.manvsclass.model.ClassUT;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface ScalataMapper {

    @Mapping(target = "name", source = "scalataName")
    @Mapping(target = "numLevels", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "associations", ignore = true)
    Scalata toEntity(ScalataDTO scalataDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scalata", source = "scalata")
    @Mapping(target = "classUT", source = "classUT")
    @Mapping(target = "level", source = "classUTScalataDTO.level")
    @Mapping(target = "timeLimit", source = "classUTScalataDTO.timeLimit")
    ClassUTScalata toAssociation(ClassUTScalataDTO classUTScalataDTO, Scalata scalata, ClassUT classUT);

    @AfterMapping
    default void setupId(@MappingTarget ClassUTScalata classUTScalata) {

        if (classUTScalata.getClassUT() != null && classUTScalata.getScalata() != null) {
            classUTScalata.setId(new ClassUTScalataId(
                    classUTScalata.getClassUT().getName(),
                    classUTScalata.getScalata().getName()
            ));
        }
    }
}