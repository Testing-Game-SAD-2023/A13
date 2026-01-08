package com.groom.manvsclass.model.dto;

import com.groom.manvsclass.model.enums.HintTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modello per il caricamento di un nuovo suggerimento tramite file JSON")
public class Hint {

    @Schema(description = "Nome della classe UT (obbligatorio solo se type=CLASS)", example = "FTPFile.java")
    private String classUTName;

    @NotNull
    @Schema(description = "Titolo del suggerimento", example = "Suggerimento Iniziale", required = true)
    private String name;

    @NotNull
    @Schema(description = "Contenuto testuale del suggerimento", example = "Controlla il costruttore della classe.", required = true)
    private String content;

    @Schema(description = "Nome del file immagine allegato (deve corrispondere al nome nel multipart)", example = "help_image.png")
    private String imageUri;

    @NotNull
    @Schema(description = "Tipo di suggerimento", example = "GENERIC", required = true)
    private HintTypeEnum type;

    @Schema(description = "Posizione nell'ordinamento (se omesso viene assegnato automaticamente)", example = "1")
    private Integer order;
}