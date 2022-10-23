package es.technest.security.api.security.signature.exception;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.config.http.exception.NotFoundException;

import static es.technest.security.api.config.http.dto.ErrorCode.NOT_FOUND_CLIENT_TOKEN;

public class UnableToObtainClientSecretException extends NotFoundException {

    private static final String MESSAGE_PATTERN = "Secret is missing for clientId=%s.";

    public UnableToObtainClientSecretException(ClientId clientId) {
        super(NOT_FOUND_CLIENT_TOKEN, String.format(MESSAGE_PATTERN, clientId));
    }
}
