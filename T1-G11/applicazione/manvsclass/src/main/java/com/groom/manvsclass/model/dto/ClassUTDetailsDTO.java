package com.groom.manvsclass.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.model.ClassUT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassUTDetailsDTO {
    private String name;
    private String difficulty;
    private String description;

    public static ClassUT parseFromJson(String jsonDetails) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonDetails, ClassUT.class);
    }
}
