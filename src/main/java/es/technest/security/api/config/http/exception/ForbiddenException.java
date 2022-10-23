package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class ForbiddenException extends WithErrorCodeException {

    public ForbiddenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ForbiddenException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}