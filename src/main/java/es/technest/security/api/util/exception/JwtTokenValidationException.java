package es.technest.security.api.util.exception;

import es.technest.security.api.config.http.exception.BadGatewayException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_INVALID_AUTH_TOKEN_SIGNATURE;

public class JwtTokenValidationException extends BadGatewayException {

    private static final String MESSAGE = "Invalid JWT token.";

    public JwtTokenValidationException(Throwable cause) {
        super(ERROR_INVALID_AUTH_TOKEN_SIGNATURE, MESSAGE, cause);
    }
}
