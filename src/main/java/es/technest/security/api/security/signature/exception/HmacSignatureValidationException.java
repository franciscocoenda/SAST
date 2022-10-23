package es.technest.security.api.security.signature.exception;

import es.technest.security.api.config.http.exception.UnauthorizedException;

import static es.technest.security.api.config.http.dto.ErrorCode.INVALID_HMAC_SIGNATURE;

public class HmacSignatureValidationException extends UnauthorizedException {

    private static final String MESSAGE_PATTERN_WITH_CLIENT_KEY = "Request for clientKey=%s was not successful because of HMAC Signature validation.";

    private static final String MESSAGE_PATTERN = "Request was not successful because of HMAC Signature validation.";

    public HmacSignatureValidationException(String clientKey) {
        super(INVALID_HMAC_SIGNATURE, String.format(MESSAGE_PATTERN_WITH_CLIENT_KEY, clientKey));
    }

    public HmacSignatureValidationException() {
        super(INVALID_HMAC_SIGNATURE, MESSAGE_PATTERN);
    }
}
