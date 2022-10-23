package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.InternalServerErrorException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_RETRIEVING_TOKEN_DATA;

public class InternalErrorRetrievingTokenDataException extends InternalServerErrorException {
    
    public InternalErrorRetrievingTokenDataException(Throwable throwable) {
        super(ERROR_RETRIEVING_TOKEN_DATA, "Error trying to retrieve data from token repository.", throwable);
    }
}
