package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.ConflictException;

import static es.technest.security.api.config.http.dto.ErrorCode.CONFLICT_EXPIRED_TOKEN;

public class ExpiredTokenException extends ConflictException {
    public ExpiredTokenException(Throwable e) {
        super(CONFLICT_EXPIRED_TOKEN, "Expired token retrieving data.", e);
    }
}
