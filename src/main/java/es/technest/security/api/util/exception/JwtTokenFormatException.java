package es.technest.security.api.util.exception;

import es.technest.security.api.config.http.exception.InternalServerErrorException;

import static es.technest.security.api.config.http.dto.ErrorCode.INVALID_TOKEN;

public class JwtTokenFormatException extends InternalServerErrorException {

    private static final String MESSAGE = "Wrong JWT token.";

    public JwtTokenFormatException() {
        super(MESSAGE);
    }

    public JwtTokenFormatException(Throwable throwable) {
        super(INVALID_TOKEN, MESSAGE, throwable);
    }
}
