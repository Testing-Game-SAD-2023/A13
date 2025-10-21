package com.example.db_setup.controller.advice;

import com.example.db_setup.model.dto.exception.ApiErrorDTO;
import com.example.db_setup.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.util.Locale;

@RestControllerAdvice
public class AuthExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);
    private final MessageSource messageSource;

    public AuthExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiErrorDTO> handlePasswordMismatchException(PasswordMismatchException e, Locale locale) {
        return ResponseEntity.badRequest().body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("UserRegistrationDTO.password.mismatch", null, locale))
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleUserAlreadyExistsException(UserAlreadyExistsException e, Locale locale) {
        return ResponseEntity.badRequest().body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("UserRegistrationDTO.email.taken", null, locale))
        );
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ApiErrorDTO> handleServiceNotAvailableException(ServiceNotAvailableException e, Locale locale) {
        return ResponseEntity.badRequest().body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("experience.init.error", null, locale))
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleUserNotFoundException(UserNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("LoginDTO.user.notfound", null, locale)));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiErrorDTO> handleMessagingException(MessagingException e, Locale locale) {
        logger.error("[requestResetPassword] Failed to send reset password email", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorDTO("none", messageSource.getMessage("emailService.messagingException", null, locale)));
    }

    @ExceptionHandler(PasswordResetTokenNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handlePasswordResetTokenNotFoundException(PasswordResetTokenNotFoundException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("passwordResetToken.incorrect", null, locale)));
    }

    @ExceptionHandler(IncompatibleEmailException.class)
    public ResponseEntity<ApiErrorDTO> handleIncompatibleEmailException(IncompatibleEmailException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("passwordResetToken.incorrect", null, locale)));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidRefreshTokenException(InvalidRefreshTokenException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiErrorDTO(e.getField(), messageSource.getMessage("refreshToken.incorrect", null, locale)));
    }
}
