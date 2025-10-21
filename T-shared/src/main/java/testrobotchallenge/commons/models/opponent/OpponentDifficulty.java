package testrobotchallenge.commons.models.opponent;

/**
 * Enumerazione che definisce i livelli di difficoltà disponibili per un avversario.
 */
public enum OpponentDifficulty {
    EASY,
    MEDIUM,
    HARD;

    /**
     * Metodo che converte la costante in un numero intero. Viene utilizzato per determinare il numero punti esperienza
     * assegnati battendo un avversario alla difficoltà specificata.
     */
    public int toInt() {
        return this.ordinal() + 1;
    }
}
