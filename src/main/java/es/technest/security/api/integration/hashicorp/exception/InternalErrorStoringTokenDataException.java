package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.InternalServerErrorException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_STORING_TOKEN_DATA;

public class InternalErrorStoringTokenDataException extends InternalServerErrorException {
    
    public InternalErrorStoringTokenDataException(Throwable throwable) {
        super(ERROR_STORING_TOKEN_DATA, "Error trying to store data into token repository.", throwable);
    }
}
