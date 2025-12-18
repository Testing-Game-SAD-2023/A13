package com.groom.manvsclass.mapper;

import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.entity.HintEntity;
import com.groom.manvsclass.model.dto.Hint;
import com.groom.manvsclass.model.dto.HintResponse;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.ClassUTRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class HintMapper {

    protected AdminRepository adminRepository;
    protected ClassUTRepository classUTRepository;

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Autowired
    public void setClassUTRepository(ClassUTRepository classUTRepository) {
        this.classUTRepository = classUTRepository;
    }

    @Mappings({
            @Mapping(source = "classUTName", target = "classUt", qualifiedByName = "mapClassUTFromName"),
            @Mapping(source = "imageUri", target = "imageUri")
    })
    public abstract HintEntity dtoToEntity(Hint hint);

    @Mappings({
            @Mapping(source = "classUt.name", target = "classUTName")
    })
    public abstract Hint entityToDto(HintEntity hintEntity);

    @Mappings({
            @Mapping(source = "admin.email", target = "adminEmail"),
            @Mapping(source = "classUt.name", target = "classUTName")
    })
    public abstract HintResponse toResponse(HintEntity hintEntity);

    public abstract List<Hint> mapList(List<HintEntity> hintEntityList);

    public abstract List<HintResponse> toResponseList(List<HintEntity> hintEntityList);

    @Named("mapClassUTFromName")
    public ClassUTEntity mapClassUTFromName(String classUTName) {
        if (classUTName == null) {
            return null;
        }
        return classUTRepository.findById(classUTName).orElse(null);
    }

}