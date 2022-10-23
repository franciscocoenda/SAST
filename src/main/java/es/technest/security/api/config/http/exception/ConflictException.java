package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class ConflictException extends WithErrorCodeException {

    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ConflictException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}