package com.groom.manvsclass.mapper;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.model.Guideline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Base64;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = { LocalDate.class, Base64.class })
public interface GuidelineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "image", source = "image", qualifiedByName = "base64ToBytes")
    Guideline toEntity(GuidelineDTO singleGuidelineDTO);

    @Mapping(target= "image", source = "image", qualifiedByName = "bytesToBase64")
    GuidelineDTO toDto(Guideline guideline);

    List<Guideline> toEntityList(List<GuidelineDTO> singleGuidelineDTOs);

    List<GuidelineDTO> toDtoList(List<Guideline> guidelines);

    @Named("base64ToBytes")
    default byte[] base64ToBytes(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        try {
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }
            return Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("bytesToBase64")
    default String bytesToBase64(byte[] bytesImage) {
        if (bytesImage == null || bytesImage.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytesImage);
    }

}