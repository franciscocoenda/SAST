package es.technest.security.api.config.http.exception;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

@Log4j2
@AllArgsConstructor
public class NoHttpResponseRetryHandler implements HttpRequestRetryHandler {

    private int maxRetries;

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= maxRetries) {
            log.warn("Retrying request exhausted attempts");
            return false;
        }

        if (exception instanceof NoHttpResponseException) {
            log.warn("Retrying request executionCount {}", executionCount);
            return true;
        }

        return false;
    }
}
