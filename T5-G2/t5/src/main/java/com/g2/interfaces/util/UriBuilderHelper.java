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

package com.g2.interfaces.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


// Costruisce un URI partendo dal baseUrl e dall'endpoint, aggiungendo eventuali
// parametri extra
public class UriBuilderHelper {

    private UriBuilderHelper() {
        throw new IllegalStateException("Classe utility per costruire un URI a partire da un baseURL e un endpoint");
    }

    public static String buildUri(String baseUrl, String endpoint, Map<String, String> queryParams) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto.");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(endpoint);

        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach((key, value) -> {
                if (key == null || key.trim().isEmpty()) {
                    throw new IllegalArgumentException("Le chiavi dei parametri non possono essere nulle o vuote.");
                }
                if (value == null) {
                    throw new IllegalArgumentException("I valori dei parametri non possono essere nulli.");
                }
                builder.queryParam(key, value);
            });
        }

        return builder.build().toUriString();
    }
}
 
