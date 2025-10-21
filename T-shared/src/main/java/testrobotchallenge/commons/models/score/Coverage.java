package testrobotchallenge.commons.models.score;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Classe che rappresenta una metrica di copertura.
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
 *
 * <p>
 * Questa classe è pensata per uso interno del dominio (principalmente persistenza) e come valore incorporabile in altre entità,
 * non come parte di un DTO per comunicazione esterna.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class Coverage {
    private int covered;
    private int missed;
}
