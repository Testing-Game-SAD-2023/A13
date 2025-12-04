package com.groom.manvsclass.api;

import com.groom.manvsclass.security.JwtRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/*
 * Questa classe definisce il template per le chiamate API interne ai moduli effettuate da T1
 */
@Component
public class RestExchangeTemplateHelper {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(RestExchangeTemplateHelper.class);

    public RestExchangeTemplateHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> exchange(
            String url,
            Map<String, String> queryParams,
            HttpMethod method,
            HttpHeaders headers,
            Object requestBody,
            Class<T> responseType
    ) {
        if (headers == null) {
            headers = new HttpHeaders();
        }

        // Aggiunge Authorization se mancante e presente nel contesto
        if (JwtRequestContext.getJwtToken() != null) {
            headers.add(HttpHeaders.COOKIE, JwtRequestContext.getJwtToken());
        }

        if (queryParams != null)
            url = extendUrlWithQueryParams(url, queryParams);

        HttpEntity<?> entity = (requestBody != null) ? new HttpEntity<>(requestBody, headers) : new HttpEntity<>(headers);

        logger.info("Sending request to {} with jwt {} and requestBody {}", url, JwtRequestContext.getJwtToken(), entity.getBody());
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);

            logger.info("Response received: {}", response);
            if (response.getStatusCode().isError()) {
                throw new com.groom.manvsclass.service.exception.ExternalServiceException(
                    String.format("Il servizio esterno ha restituito un errore (%s): %s. " +
                        "Verificare che tutti i servizi siano attivi e raggiungibili.",
                        response.getStatusCode(), url)
                );
            }

            return response;
        } catch (RestClientException e) {
            logger.error("REST call failed to {}: {}", url, e.getMessage(), e);
            throw new com.groom.manvsclass.service.exception.ExternalServiceException(
                "Impossibile comunicare con il servizio esterno (" + url + "): " + e.getMessage() + ". " +
                "Verificare la connessione di rete e che il servizio sia attivo.",
                e
            );
        }
    }

    private String extendUrlWithQueryParams(String url, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        queryParams.forEach(builder::queryParam);

        return builder.toUriString();
    }
}

