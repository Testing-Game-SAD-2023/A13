package testrobotchallenge.commons.models.opponent;

/**
 * Enumerazione che definisce le modalità di gioco attualmente disponibili.
 */
public enum GameMode {
    /*
     * Il nome delle costanti non rispetta la nomenclatura di Java in quanto l'enumerazione è stata introdotta solo alla
     * fine dello sviluppo del GameEngine, dove si usavano direttamente le stringhe "PartitaSingola", "Allenamento" e
     * "Scalata". I nomi sono stati così per poterli richiamare direttamente per velocità, senza definire e passare per metodi di conversione
     * PARTITA_SINGOLA -> "PartitaSingola"
     */
    /* TODO: ridefinire le costanti nella nomenclatura corretta, aggiungere i metodi di conversione e applicarli ove necessario*/
    PartitaSingola,
    Allenamento,
    Scalata
}
