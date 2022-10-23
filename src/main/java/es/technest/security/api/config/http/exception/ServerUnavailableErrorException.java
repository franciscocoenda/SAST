package es.technest.security.api.config.http.exception;


import es.technest.security.api.config.http.dto.ErrorCode;

public class ServerUnavailableErrorException extends WithErrorCodeException {

    public ServerUnavailableErrorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
