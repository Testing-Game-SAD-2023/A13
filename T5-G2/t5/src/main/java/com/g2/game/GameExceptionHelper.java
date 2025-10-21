package com.g2.game;

import com.g2.language.MessageKey;
import com.g2.language.MessageResolver;
import com.g2.session.exception.GameModeAlreadyExistException;
import com.g2.session.exception.GameModeDontExistException;
import com.g2.session.exception.SessionDoesntExistException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@AllArgsConstructor
public class GameExceptionHelper {

    private static final Logger logger = LoggerFactory.getLogger(GameExceptionHelper.class);
    private final MessageResolver messageResolver;

    @ExceptionHandler(GameModeAlreadyExistException.class)
    public ResponseEntity<ApiErrorBackend> handleGameModeAlreadyExistException(GameModeAlreadyExistException e, Locale locale) {
        logger.warn(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiErrorBackend(messageResolver.resolve(MessageKey.GAMEMODE_ALREADY_EXISTS, locale)));
    }

    @ExceptionHandler(GameModeDontExistException.class)
    public ResponseEntity<ApiErrorBackend> handleGameModeDontExistException(GameModeAlreadyExistException e, Locale locale) {
        logger.warn(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorBackend(messageResolver.resolve(MessageKey.GAMEMODE_DOESNT_EXIST, locale)));
    }

    @ExceptionHandler(SessionDoesntExistException.class)
    public ResponseEntity<ApiErrorBackend> handleSessionDoesntExistException(SessionDoesntExistException e, Locale locale) {
        logger.error(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorBackend(messageResolver.resolve(MessageKey.SESSION_DOESNT_EXIST, locale)));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRestClientException(RestClientException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", ex.getMessage());

        StackTraceElement relevantStackTrace = ex.getStackTrace()[0];
        logger.error("RestClientException: {}", relevantStackTrace);

        int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (ex.getCause() instanceof HttpStatusCodeException cause) {
            httpStatusCode = cause.getStatusCode().value();
        }

        return ResponseEntity.status(httpStatusCode).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        /*
         * Ottengo gli errori per ogni binding di un json e popolo la mappa così posso poi inviarla
         */
        ex.getBindingResult().getFieldErrors().forEach(error
                -> errors.put(error.getField(), error.getDefaultMessage())
        );
        logger.error("Errore durante la validazione della parametri: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /*
     * Handler eccezione runtime
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        // Recupera il messaggio dell'errore
        errorResponse.put("error", ex.getMessage());

        // Recupera la riga più importante dello stacktrace
        StackTraceElement relevantStackTrace = ex.getStackTrace()[0];
        errorResponse.put("cause", relevantStackTrace.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
