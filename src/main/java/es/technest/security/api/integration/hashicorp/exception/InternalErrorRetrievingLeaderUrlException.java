package es.technest.security.api.integration.hashicorp.exception;

import es.technest.security.api.config.http.exception.InternalServerErrorException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_RETRIEVING_LEADER_URL;

public class InternalErrorRetrievingLeaderUrlException extends InternalServerErrorException {

    public InternalErrorRetrievingLeaderUrlException(Throwable throwable) {
        super(ERROR_RETRIEVING_LEADER_URL, "Error trying to retrieve leader url.", throwable);
    }
}
