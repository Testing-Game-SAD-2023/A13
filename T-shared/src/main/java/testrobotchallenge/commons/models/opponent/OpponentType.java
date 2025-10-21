package testrobotchallenge.commons.models.opponent;

/**
 * Enumerazione che definisce il tipo di avversari disponibili per una classe sotto test (classe UT).
 * <p>
 * Ogni costante rappresenta la sorgente del test JUnit che funge da avversario per l’utente:
 * <ul>
 *   <li>{@link #EVOSUITE} → Avversario generato tramite il tool EvoSuite.</li>
 *   <li>{@link #RANDOOP} → Avversario generato tramite il tool Randoop.</li>
 *   <li>{@link #STUDENT} → Avversario derivato dai test scritti da altri studenti.</li>
 *   <li>{@link #LLM} → Avversario generato tramite modelli di intelligenza artificiale (Large Language Model).</li>
 * </ul>
 */
public enum OpponentType {
    EVOSUITE,
    RANDOOP,
    STUDENT,
    GEMINI,
    GEMINI_COVERAGE,
    LLM;

    /**
     * Metodo che restituisce la costante {@link OpponentType} corrispondente alla stringa passata, ignorando maiuscole e minuscole.
     *
     * @param name il nome del tipo di avversario (case-insensitive), ad esempio "evosuite" o "STUDENT"
     * @return la costante {@link OpponentType} corrispondente
     * @throws IllegalArgumentException se il valore passato non corrisponde a nessun tipo valido
     */
    public static OpponentType fromStringIgnoreCase(String name) {
        for (OpponentType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown OpponentType: " + name);
    }
}
