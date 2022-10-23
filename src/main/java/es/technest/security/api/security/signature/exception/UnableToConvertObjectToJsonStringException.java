package es.technest.security.api.security.signature.exception;


import es.technest.security.api.config.http.exception.InternalServerErrorException;

import static es.technest.security.api.config.http.dto.ErrorCode.ERROR_OBJECT_TO_JSON_STRING;

public class UnableToConvertObjectToJsonStringException extends InternalServerErrorException {

    private static final String MESSAGE = "Unable to convert Object=%s to Json string";

    public UnableToConvertObjectToJsonStringException(Object object) {
        super(ERROR_OBJECT_TO_JSON_STRING, String.format(MESSAGE, object));
    }
}
