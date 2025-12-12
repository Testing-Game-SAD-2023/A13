package com.groom.manvsclass.service.exception;

/**
 * Base exception class that supports localized error messages.
 * Stores a message key for i18n and optional parameters for message formatting,
 * while keeping the detailed verbose message for logging purposes.
 */
public abstract class LocalizedException extends RuntimeException {
    
    private final String messageKey;
    private final transient Object[] messageParams;
    
    /**
     * Constructor with message key only
     * @param detailedMessage Detailed message for logging (will be stored in super.getMessage())
     * @param messageKey Message key for i18n lookup
     */
    protected LocalizedException(String detailedMessage, String messageKey) {
        super(detailedMessage);
        this.messageKey = messageKey;
        this.messageParams = null;
    }
    
    /**
     * Constructor with message key and parameters
     * @param detailedMessage Detailed message for logging (will be stored in super.getMessage())
     * @param messageKey Message key for i18n lookup
     * @param messageParams Parameters to be used with the localized message
     */
    protected LocalizedException(String detailedMessage, String messageKey, Object... messageParams) {
        super(detailedMessage);
        this.messageKey = messageKey;
        this.messageParams = messageParams;
    }
    
    /**
     * Constructor with message key, parameters, and cause
     * @param detailedMessage Detailed message for logging
     * @param messageKey Message key for i18n lookup
     * @param cause The cause of the exception
     * @param messageParams Parameters to be used with the localized message
     */
    protected LocalizedException(String detailedMessage, String messageKey, Throwable cause, Object... messageParams) {
        super(detailedMessage, cause);
        this.messageKey = messageKey;
        this.messageParams = messageParams;
    }
    
    /**
     * Gets the message key for i18n lookup
     * @return The message key
     */
    public String getMessageKey() {
        return messageKey;
    }
    
    /**
     * Gets the parameters for message formatting
     * @return The message parameters, or null if none
     */
    public Object[] getMessageParams() {
        return messageParams;
    }
    
    /**
     * Returns true if this exception has a message key for localization
     * @return true if messageKey is not null
     */
    public boolean hasMessageKey() {
        return messageKey != null;
    }
}
