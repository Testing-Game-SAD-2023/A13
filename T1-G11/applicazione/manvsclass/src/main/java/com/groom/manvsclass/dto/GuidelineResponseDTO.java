package com.groom.manvsclass.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GuidelineResponseDTO {

    private Long id;
    private String title;
    private String hint;
    private byte[] image;
    private LocalDate date;
}