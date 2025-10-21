/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.g2.interfaces;

import com.g2.model.OpponentSummary;
import com.g2.model.Team;
import com.g2.model.dto.ResponseTeamComplete;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.List;
import java.util.Map;

@Service
public class T1Service extends BaseService {

    /*
     * Per il test locale senza container docker usare:
     * BASE_URL = "http://127.0.0.1:8090"
     */
    private static final String BASE_URL = "http://api_gateway-controller:8090";
    private static final String SERVICE_PREFIX = "adminService";

    private static final String STUDENT_ID_FIELD = "studentId"; // variabile introdotta per rimuovere l'issue di SonarQube sul numero di ripetizioni della stringa

    public T1Service(RestTemplate restTemplate) {

        super(restTemplate, BASE_URL + "/" + SERVICE_PREFIX);

        // Registrazione delle azioni
        registerAction("getClasses", new ServiceActionDefinition(
                params -> getClasses() // Metodo senza argomenti
        ));

        registerAction("getClassUnderTest", new ServiceActionDefinition(
                params -> getClassUnderTest((String) params[0]),
                String.class));

        registerAction("ottieniTeamByStudentId", new ServiceActionDefinition(
                params -> ottieniTeamByStudentId((String) params[0]),
                String.class));

        registerAction("ottieniIdTeamByStudentId", new ServiceActionDefinition(
                params -> ottieniIdTeamByStudentId((String) params[0]),
                String.class));
        registerAction("OttieniTeamCompleto", new ServiceActionDefinition(
                params -> ottieniTeamCompleto((String) params[0]),
                String.class));


        registerAction("getOpponentsSummary", new ServiceActionDefinition(
                params -> getOpponentsSummary()
        ));

        registerAction("getOpponentCoverage", new ServiceActionDefinition(
                params -> getOpponentCoverage((String) params[0], (String) params[1], (OpponentDifficulty) params[2]),
                String.class, String.class, OpponentDifficulty.class));

        registerAction("getOpponentJacocoScore", new ServiceActionDefinition(
                params -> getOpponentJacocoScore((String) params[0], (String) params[1], (OpponentDifficulty) params[2]),
                String.class, String.class, OpponentDifficulty.class));

        registerAction("getOpponentEvosuiteScore", new ServiceActionDefinition(
                params -> getOpponentEvosuiteScore((String) params[0], (String) params[1], (OpponentDifficulty) params[2]),
                String.class, String.class, OpponentDifficulty.class));
    }


    // Restituisce il file di coverage dell'avversario scelto
    private String getOpponentCoverage(String classUT, String type, OpponentDifficulty difficulty) {
        return callRestGET("/opponents/%s/%s/%s/coverage".formatted(classUT, type, difficulty), null, String.class);
    }

    // Restituisce il punteggio di Evosuite ottenuto dall'avversario scelto
    private EvosuiteScoreDTO getOpponentEvosuiteScore(String classUT, String type, OpponentDifficulty difficulty) {
        return callRestGET("/opponents/%s/%s/%s/score/evosuite".formatted(classUT, type, difficulty), null, EvosuiteScoreDTO.class);
    }

    // Restituisce il punteggio di Jacoco ottenuto dall'avversario scelto
    private JacocoScoreDTO getOpponentJacocoScore(String classUT, String type, OpponentDifficulty difficulty) {
        return callRestGET("/opponents/%s/%s/%s/score/jacoco".formatted(classUT, type, difficulty), null, JacocoScoreDTO.class);
    }

    // Metodi effettivi
    private List<String> getClasses() {
        return callRestGET("/opponents/classes/summary", null, new ParameterizedTypeReference<List<String>>() {
        });
    }

    // Restituisce gli avversari disponibili come tupla (gameMode, opponentType, opponentDifficulty)
    private List<OpponentSummary> getOpponentsSummary() {
        final String endpoint = "/opponents/summary";
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<OpponentSummary>>() {
        });
    }

    private String getClassUnderTest(String nomeCUT) {
        byte[] result = callRestGET("/opponents/downloadFile/" + nomeCUT, null, byte[].class);
        return removeBOM(convertToString(result));
    }

    private Team ottieniTeamByStudentId(String studentId) {
        Map<String, String> queryParams = Map.of(
                STUDENT_ID_FIELD, studentId
        );
        return callRestGET("/ottieniTeamByStudentId", queryParams, Team.class);
    }

    private String ottieniIdTeamByStudentId(String studentId) {
        Map<String, String> queryParams = Map.of(
                STUDENT_ID_FIELD, studentId
        );
        Team team = callRestGET("/ottieniTeamByStudentId", queryParams, Team.class);
        return team.getIdTeam();
    }

    private ResponseTeamComplete ottieniTeamCompleto(String studentId) {
        Map<String, String> queryParams = Map.of(
                STUDENT_ID_FIELD, studentId
        );
        return callRestGET("/ottieniDettagliTeamCompleto", queryParams, ResponseTeamComplete.class);
    }


}
