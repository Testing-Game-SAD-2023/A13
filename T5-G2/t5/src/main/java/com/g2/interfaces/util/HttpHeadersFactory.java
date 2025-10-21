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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public class HttpHeadersFactory {

    private HttpHeadersFactory() {
        throw new IllegalStateException("Classe utility per la generazione dell'header HTTP");
    }

    public static HttpHeaders createHeaders(Map<String, String> customHeaders, MediaType defaultContentType) {
        HttpHeaders headers = new HttpHeaders();

        if (customHeaders != null) {
            customHeaders.forEach(headers::add);
        }

        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(defaultContentType);
        }

        return headers;
    }
}
 