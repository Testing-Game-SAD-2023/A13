package com.groom.manvsclass.mapper;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.dto.ScalataDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScalataMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {})
    ScalataDTO scalatatoScalataDTO(Scalata scalata);
    Scalata scalatafromScalataDTO(ScalataDTO scalataDTO);
}
