package es.technest.security.api.config.http.exception;


import es.technest.security.api.config.http.dto.ErrorCode;

public class WithErrorCodeException extends RuntimeException {

    private ErrorCode errorCode;

    public WithErrorCodeException(String message) {
        super(message);
    }

    public WithErrorCodeException(Throwable cause) {
        super(cause);
    }

    public WithErrorCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public WithErrorCodeException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
