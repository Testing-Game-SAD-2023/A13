package com.groom.manvsclass.model.dto;

import com.groom.manvsclass.model.enums.HintTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per la risposta dei dettagli di un suggerimento")
public class HintResponse {

    @Schema(description = "Identificativo univoco del suggerimento", example = "1")
    private Long id;

    @Schema(description = "Email dell'amministratore che ha creato il suggerimento", example = "admin@test.it")
    private String adminEmail;

    @Schema(description = "Nome della Classe Under Test associata", example = "FTPFile.java")
    private String classUTName;

    @Schema(description = "Titolo del suggerimento", example = "Verifica Permessi")
    private String name;

    @Schema(description = "Testo esteso del suggerimento", example = "Assicurati che l'utente abbia i permessi di scrittura.")
    private String content;

    @Schema(description = "Percorso dell'immagine associata nel file system", example = "/uploads/170456789_help.png")
    private String imageUri;

    @Schema(description = "Tipologia di suggerimento", example = "CLASS", allowableValues = {"GENERIC", "CLASS"})
    private HintTypeEnum type;

    @Schema(description = "Ordine di visualizzazione all'interno della categoria", example = "1")
    private Integer order;

    @Schema(description = "Timestamp di creazione", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp dell'ultimo aggiornamento", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}