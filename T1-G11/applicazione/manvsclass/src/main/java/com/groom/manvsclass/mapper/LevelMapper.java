package com.groom.manvsclass.mapper;

import com.groom.manvsclass.model.Level;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.dto.LevelDTO;
import com.groom.manvsclass.model.dto.ScalataDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LevelMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {})
    LevelDTO leveltoLevelDTO(Level level);
}
