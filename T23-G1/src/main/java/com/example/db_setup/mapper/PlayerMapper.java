package com.example.db_setup.mapper;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.dto.gamification.PlayerDTO;
import com.example.db_setup.model.dto.gamification.PlayerProgressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PlayerProgressMapper.class)
public interface PlayerMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"password"})
    @Mapping(source = "player.userProfile.name", target = "name")
    @Mapping(source = "player.userProfile.surname", target = "surname")
    @Mapping(source = "player.userProfile.nickname", target = "nickname")
    @Mapping(source = "player.userProfile.email", target = "email")
    @Mapping(source="ID", target="id")
    PlayerDTO playerToPlayerDTO(Player player);
}