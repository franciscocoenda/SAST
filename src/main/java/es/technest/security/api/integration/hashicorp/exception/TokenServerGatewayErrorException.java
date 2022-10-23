package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.BadGatewayException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_IN_TOKEN_SERVICE;

public class TokenServerGatewayErrorException extends BadGatewayException {
    
    public TokenServerGatewayErrorException(Throwable throwable) {
        super(ERROR_IN_TOKEN_SERVICE, "Error using token repository service", throwable);
    }
}
