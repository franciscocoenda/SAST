package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.BadGatewayException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_IN_LEADER_PROVIDER_SERVICE;

public class LeaderProviderServerGatewayErrorException extends BadGatewayException {

    public LeaderProviderServerGatewayErrorException(Throwable throwable) {
        super(ERROR_IN_LEADER_PROVIDER_SERVICE, "Error using leader provider service.", throwable);
    }
}
