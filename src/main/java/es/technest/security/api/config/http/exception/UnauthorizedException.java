package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class UnauthorizedException extends WithErrorCodeException {

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
