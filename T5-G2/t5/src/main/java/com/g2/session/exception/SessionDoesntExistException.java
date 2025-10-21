package com.g2.session.exception;

/**
 * Eccezione personalizzata che viene lanciata quando una sessione non esiste.
 */
public class SessionDoesntExistException extends RuntimeException {

    // Costruttore che accetta un messaggio di errore
    public SessionDoesntExistException(String message) {
        super(message); // Passa il messaggio alla classe madre (RuntimeException)
    }

    // Costruttore che accetta un messaggio di errore e una causa (eccezione originale)
    public SessionDoesntExistException(String message, Throwable cause) {
        super(message, cause); // Passa sia il messaggio che la causa alla classe madre
    }
}
