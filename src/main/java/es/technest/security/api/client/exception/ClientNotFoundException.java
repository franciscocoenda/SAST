package es.technest.security.api.client.exception;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.config.http.exception.NotFoundException;

import static es.technest.security.api.config.http.dto.ErrorCode.NOT_FOUND_CLIENT;

public class ClientNotFoundException extends NotFoundException {

    private static final String MESSAGE_FOR_CLIENT_KEY = "Client not found with clientKey=%s";
    private static final String MESSAGE_FOR_CLIENT_ID = "Client not found with clientId=%s";

    public ClientNotFoundException(String clientKey) {
        super(NOT_FOUND_CLIENT, String.format(MESSAGE_FOR_CLIENT_KEY, clientKey));
    }

    public ClientNotFoundException(ClientId clientId) {
        super(NOT_FOUND_CLIENT, String.format(MESSAGE_FOR_CLIENT_ID, clientId.toValue()));
    }
}
