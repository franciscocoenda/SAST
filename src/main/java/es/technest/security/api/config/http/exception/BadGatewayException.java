package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class BadGatewayException extends WithErrorCodeException {

    public BadGatewayException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public BadGatewayException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

