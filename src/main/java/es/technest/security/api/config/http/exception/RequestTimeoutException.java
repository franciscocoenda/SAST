package es.technest.security.api.config.http.exception;

public class RequestTimeoutException extends RuntimeException {

    public RequestTimeoutException(String message) {
        super(message);
    }

}
