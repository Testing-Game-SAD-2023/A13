package com.robotchallenge.t8.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO utilizzato per richiedere il calcolo della coverage
 * di un test prodotto dall'avversario.
 *
 * <p>
 * Contiene le informazioni necessarie a Evosuite per eseguire la coverage sull progetto Maven ricevuto assieme alla richiesta:
 * <ul>
 *   <li>Il nome della classe sotto test;</li>
 *   <li>Il package a cui appartiene;</li>
 *   <li>Il nome del test JUnit associato scritto dal giocatore;</li>
 *   <li>Il codice della classe sotto test;<li/>
 *   <li>Il codice del test JUnit.<li/>
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
@ToString
@NoArgsConstructor
public class StudentCoverageRequestDTO {
    @JsonProperty("testClassName")
    String testClassName;

    @JsonProperty("testClassCode")
    String testClassCode;

    @JsonProperty("classUTName")
    private String classUTName;

    @JsonProperty("classUTCode")
    private String classUTCode;

    @JsonProperty("classUTPackage")
    private String classUTPackage;

}
