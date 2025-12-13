/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
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

package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.model.OpponentSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per i dati di un livello di una Scalata restituiti da T1.
 * <p>
 * Questo DTO rappresenta i dati di un singolo livello all'interno di una Scalata,
 * includendo informazioni come la classe da testare, il tempo massimo disponibile,
 * e il nome dell'avversario.
 * 
 * NOTA: T1 invia l'oggetto Opponent completo (con id, createdAt, coverage, 
 * jacocoScore, evosuiteScore) ma T5 riceve solo i campi essenziali tramite OpponentSummary.
 * I campi extra vengono ignorati automaticamente da Jackson.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelDataDTO {
    
    /**
     * Tempo massimo disponibile per completare il livello, espresso in secondi.
     */
    @JsonProperty("tempoMax")
    private int tempoMax;
    
    /**
     * Avversario per questo livello.
     */
    @JsonProperty("opponent")
    private OpponentSummary opponentName;
}
