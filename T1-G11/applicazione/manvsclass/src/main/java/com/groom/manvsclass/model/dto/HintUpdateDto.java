package com.groom.manvsclass.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dati per l'aggiornamento di un suggerimento esistente")
public class HintUpdateDto {

    @Schema(description = "Nuovo contenuto testuale", example = "Contenuto aggiornato.")
    private String content;

    @Schema(description = "Nuova URI per l'immagine", example = "/uploads/new_image.jpg")
    private String imageUri;

    @Schema(description = "Nuova posizione nell'ordinamento", example = "2")
    private Integer order;
}