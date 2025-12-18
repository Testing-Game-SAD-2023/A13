package com.groom.manvsclass.model.dto;

import com.groom.manvsclass.model.enums.HintTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintResponse {

    private Long id;
    private String adminEmail;
    private String classUTName; //DA MODIFICARE CI DEVE ANDARE ClassUT
    private String name;
    private String content;
    private String imageUri;
    private HintTypeEnum type;
    private Integer order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
