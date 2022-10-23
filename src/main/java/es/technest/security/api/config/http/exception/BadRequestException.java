package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class BadRequestException extends WithErrorCodeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
