package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class UnprocessableEntityException extends WithErrorCodeException {

    public UnprocessableEntityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
