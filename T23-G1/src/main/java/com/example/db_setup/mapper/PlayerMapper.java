package com.example.db_setup.mapper;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.dto.gamification.PlayerDTO;
import com.example.db_setup.model.dto.gamification.PlayerProgressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PlayerProgressMapper.class)
public interface PlayerMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"ID", "password", "userProfile")
    PlayerDTO playerToPlayerDTO(Player player){
        return new PlayerDTO(
            player.getID(),
            player.getName(),
            player.getSurname(),
            player.getNickname(),
            player.getEmail(),
            player.getStudies().toString(),

        )
    }
}
