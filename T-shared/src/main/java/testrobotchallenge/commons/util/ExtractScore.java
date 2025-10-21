package testrobotchallenge.commons.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Classe utility usata per estrarre le metriche di copertura dai report di test.
 * <p>
 * Supporta:
 * <ul>
 *     <li>Report generati da EvoSuite (CSV o simili).</li>
 *     <li>Report generati da JaCoCo (XML).</li>
 * </ul>
 */
public class ExtractScore {
    private static final Logger logger = LoggerFactory.getLogger(ExtractScore.class);

    private ExtractScore() {
        throw new IllegalStateException("Classe utility usata per estrarre le metriche di copertura dai report di test");
    }

    /* TODO: rimuovere i throw RuntimeException e sostuirle con una gestione nei moduli chiamanti */

    /**
     * Estrae le informazioni di copertura da un report csv generato da EvoSuite.
     * <p>
     * Restituisce un array bidimensionale, dove:
     * <ul>
     *     <li>Ogni riga corrisponde a un criterio di EvoSuite, nell'ordine riportato nel csv;</li>
     *     <li>La prima colonna riporta le istruzioni coperte</li>
     *     <li>La seconda colonna riporta le istruzioni mancanti</li>
     * </ul>
     *
     * @param content contenuto del report in formato csv generato EvoSuite
     * @return array bidimensionale di interi rappresentante istruzioni coperte e mancate per ogni criterio
     * @throws RuntimeException se non è possibile convertire i valori numerici
     */
    public static int[][] fromEvosuite(String content) {
        int[][] values = new int[8][8];
        String line;
        String delimiter = ",";

        if (content == null) {
            return values;
        }

        try (BufferedReader br = new BufferedReader(new StringReader(content))) {
            br.readLine(); // salto la prima riga, che contiene i nomi delle colonne

            for (int i = 0; i < 8; i++) {
                line = br.readLine();
                logger.info("line " + i + ": " + line);

                String[] columns = line.split(delimiter);

                // Verifico che esistano almeno 5 colonne, la percentuale di coverage si trova sulla terza
                if (columns.length >= 5) {
                    try {
                        values[i] = new int[]{Integer.parseInt(columns[4]), Integer.parseInt(columns[3]) - Integer.parseInt(columns[4])};
                        //double value = Double.parseDouble(columns[2].trim()) * 100;
                    } catch (NumberFormatException e) {
                        logger.error("[extractScore] Errore durante la conversione dei valori di copertura percentuale: {}", e.getMessage());
                        throw new RuntimeException("[extractScore] Errore durante la conversione dei valori di copertura percentuale: " + e.getMessage(), e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("[extractScore] Errore durante l'estrazione della colonna della copertura percentuale: {}", e.getMessage());
            throw new RuntimeException("[extractScore] Errore durante l'estrazione della colonna della copertura percentuale: " + e.getMessage(), e);
        }

        // Converto la lista in array di interi
        return values;
    }

    /**
     * Estrae le informazioni di copertura da un report JaCoCo in formato XML.
     * <p>
     * Per ogni tipo di copertura (LINE, BRANCH, INSTRUCTION) restituisce un array di due interi dove:
     * <ul>
     *     <li>L'indice 0 riporta gli elementi coperti</li>
     *     <li>L'indice 1 riporta gli elementi mancanti</li>
     * </ul>
     *
     * @param xmlContent contenuto del report JaCoCo in formato XML
     * @return array di interi bidimensionale contenente i valori di copertura
     */
    public static int[][] fromJacoco(String xmlContent) {
        final String[] coverageTypes = new String[]{"LINE", "BRANCH", "INSTRUCTION"};
        int[][] values = new int[coverageTypes.length][coverageTypes.length];

        if (xmlContent == null) {
            logger.error("[ExtractScore.fromJacoco] xmlContent is null ");
            return values;
        }

        Document doc = Jsoup.parse(xmlContent, "", Parser.xmlParser());
        for (int i = 0; i < coverageTypes.length; i++) {
            String coverageType = coverageTypes[i];
            Element counter = doc.selectFirst("report > counter[type=" + coverageType + "]");
            if (counter == null) {
                logger.error("[ExtractScore.fromJacoco] Counter extracted for {} is null", coverageType);
                values[i] = new int[]{0, 0};
                continue;
            }
            int covered = Integer.parseInt(counter.attr("covered"));
            int missed = Integer.parseInt(counter.attr("missed"));
            values[i] = new int[]{covered, missed};
            logger.info("[ExtractScore.fromJacoco] Score for {} is {}", coverageType, values[i]);
        }

        return values;
    }
}
