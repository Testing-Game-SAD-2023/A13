package com.g2.Interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceManagerLogger {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManagerLogger.class);

    private void logServiceCreation(String serviceName) {
        logger.info("[SERVICE CREATION] Creazione del servizio: {}", serviceName);
    }

    private void logHandleRequest(String serviceName, String action) {
        logger.info("[HANDLE REQUEST] Richiesta gestita dal servizio: {}, Azione: {}", serviceName, action);
    }

    private void logServiceNotFound(String serviceName) {
        logger.error("[SERVICE ERROR] Servizio non trovato: {}", serviceName);
    }

    //Log nel caso di eccezione 
    public void logMessagge(String message, Throwable error) {
        logger.error(message, error);
    }

    //log in tutte le altre occasioni 
    public void logMessagge(String Case, String serviceName, String... params){
        switch (Case) {
            case "ServiceCreation" -> {
                logServiceCreation(serviceName);
            }
            case "ServiceNotFound" -> {
                logServiceNotFound(serviceName);
            }
            case "HandleRequest" -> {
                logHandleRequest(serviceName, params[0]);
            }
        }
    }

}