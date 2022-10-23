package es.technest.security.api.security.nonce.exception;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.config.http.exception.ConflictException;

import static es.technest.security.api.config.http.dto.ErrorCode.CONFLICT_INVALID_NONCE;

public class NonceIsNotValidException extends ConflictException {

    private static final String MESSAGE = "Client request form clientId=%s with nonce=%s is not valid.";

    public NonceIsNotValidException(ClientId clientId, Long nonce) {
        super(CONFLICT_INVALID_NONCE, String.format(MESSAGE, clientId.getId().toString(), nonce));
    }
}
