package com.robotchallenge.t8.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizzato per richiedere il calcolo della coverage
 * di un test prodotto dall'avversario.
 *
 * <p>
 * Contiene le informazioni necessarie a Evosuite per eseguire la coverage sull progetto Maven ricevuto assieme alla richiesta:
 * <ul>
 *   <li>Il nome della classe,</li>
 *   <li>Il package a cui appartiene.</li>
 * </ul>
 * </p>
 *
 * <p>
 * La classe utilizza le annotazioni Lombok {@link Getter}, {@link Setter} e {@link NoArgsConstructor}
 * per la generazione automatica dei metodi di accesso e del costruttore senza argomenti necessari a Jackson per la conversione
 * in oggetto.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
public class OpponentCoverageRequestDTO {
    @JsonProperty("classUTName")
    private String classUTName;

    @JsonProperty("classUTPackage")
    private String classUTPackage;

    @Override
    public String toString() {
        return "OpponentCoverageRequestDTO{" +
                "classUTName='" + classUTName + '\'' +
                ", classUTPackage='" + classUTPackage + '\'' +
                '}';
    }
}
