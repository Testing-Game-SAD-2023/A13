package com.robotchallenge.t8.util;

import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.CoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.util.ExtractScore;

/**
 * Classe utility per costruire DTO a partire dai risultati
 * generati da EvoSuite durante l'esecuzione dei test.
 * <p>
 * Fornisce metodi statici per trasformare i dati di EvoSuite in oggetti {@link EvosuiteScoreDTO} e
 * {@link EvosuiteCoverageDTO}, usati per comunicare con il resto dei servizi.
 * </p>
 *
 * <p>Questa classe non è istanziabile e offre solamente metodi statici</p>
 */
public class BuildResponse {

    // Costruttore privato aggiunto per rimuovere l'issue "Utility classes should not have public constructors"
    // identificata da SonaQube IDE
    private BuildResponse() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Costruisce un oggetto {@link EvosuiteScoreDTO} a partire dal risultato
     * testuale generato da EvoSuite.
     *
     * @param result il contenuto del file di output EvoSuite
     * @return un {@link EvosuiteScoreDTO} contenente le metriche di copertura
     * (linee, branch, eccezioni, mutazioni, output, ...)
     */
    public static EvosuiteScoreDTO buildDTO(String result) {
        int[][] score = ExtractScore.fromEvosuite(result);

        EvosuiteScoreDTO responseBody = new EvosuiteScoreDTO();
        responseBody.setLineCoverageDTO(new CoverageDTO(
                score[0][0], score[0][1]
        ));
        responseBody.setBranchCoverageDTO(new CoverageDTO(
                score[1][0], score[1][1]
        ));
        responseBody.setExceptionCoverageDTO(new CoverageDTO(
                score[2][0], score[2][1]
        ));
        responseBody.setWeakMutationCoverageDTO(new CoverageDTO(
                score[3][0], score[3][1]
        ));
        responseBody.setOutputCoverageDTO(new CoverageDTO(
                score[4][0], score[4][1]
        ));
        responseBody.setMethodCoverageDTO(new CoverageDTO(
                score[5][0], score[5][1]
        ));
        responseBody.setMethodNoExceptionCoverageDTO(new CoverageDTO(
                score[6][0], score[6][1]
        ));
        responseBody.setCBranchCoverageDTO(new CoverageDTO(
                score[7][0], score[7][1]
        ));

        return responseBody;
    }

    /**
     * Costruisce un oggetto {@link EvosuiteCoverageDTO} esteso,
     * che include sia il DTO delle metriche ({@link EvosuiteScoreDTO}),
     * sia il contenuto raw del file di risultato EvoSuite.
     *
     * @param result il contenuto del file di output EvoSuite
     * @return un {@link EvosuiteCoverageDTO} contenente sia i punteggi elaborati
     * sia il risultato originale in formato testuale
     */
    public static EvosuiteCoverageDTO buildExtendedDTO(String result) {
        EvosuiteCoverageDTO responseBody = new EvosuiteCoverageDTO();
        responseBody.setEvosuiteScoreDTO(buildDTO(result));
        responseBody.setResultFileContent(result);

        return responseBody;
    }


}
