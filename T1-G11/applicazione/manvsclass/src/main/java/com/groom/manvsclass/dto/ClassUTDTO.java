package com.groom.manvsclass.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ClassUTDTO {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String difficulty;
    private String uri;
    private String description;
    private List<String> category;
}