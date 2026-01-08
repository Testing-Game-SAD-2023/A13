package com.groom.manvsclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamUpdate {
    private String idTeam;
    private String newName;
}
