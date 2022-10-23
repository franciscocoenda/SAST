package es.technest.security.api.config.http.exception;

import es.technest.security.api.config.http.dto.ErrorCode;

public class InternalServerErrorException extends WithErrorCodeException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(Throwable throwable) {
        super(throwable);
    }

    public InternalServerErrorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InternalServerErrorException(ErrorCode errorCode, String message, Throwable throwable) {
        super(errorCode, message, throwable);
    }
}
