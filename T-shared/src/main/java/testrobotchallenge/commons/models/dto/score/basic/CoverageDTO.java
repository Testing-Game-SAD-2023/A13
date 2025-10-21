package testrobotchallenge.commons.models.dto.score.basic;

import lombok.*;

/**
 * DTO che rappresenta una metrica di copertura.
 * <p>
 * Ogni metrica di copertura è definita da due valori interi:
 * </p>
 * <ul>
 *   <li><b>covered</b>: numero di elementi coperti dai test;</li>
 *   <li><b>missed</b>: numero di elementi non coperti dai test.</li>
 * </ul>
 *
 * <p>
 * La somma dei due valori rappresenta il totale degli elementi analizzati
 * per la metrica interessata.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoverageDTO {
    private int covered;
    private int missed;
}
