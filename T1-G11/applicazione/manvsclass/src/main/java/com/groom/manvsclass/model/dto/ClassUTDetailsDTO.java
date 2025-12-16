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
        if (jsonDetails == null || jsonDetails.trim().isEmpty()) {
            throw new IOException(
                "I dettagli della classe non sono stati forniti. " +
                "Verificare che tutti i campi del form siano compilati correttamente."
            );
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassUT result = mapper.readValue(jsonDetails, ClassUT.class);
            
            if (result.getName() == null || result.getName().trim().isEmpty()) {
                throw new IOException(
                    "Il nome della classe è obbligatorio. " +
                    "Compilare il campo 'Class Name' nel form."
                );
            }
            
            return result;
        } catch (IOException e) {
            throw new IOException(
                "Impossibile interpretare i dettagli della classe: " + e.getMessage() + ". " +
                "I dati del form potrebbero essere corrotti. Riprovare.",
                e
            );
        }
    }
}
