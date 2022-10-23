package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class NotFoundException extends WithErrorCodeException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
