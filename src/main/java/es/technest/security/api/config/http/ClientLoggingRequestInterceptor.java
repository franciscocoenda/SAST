package es.technest.security.api.config.http;

import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


@Log4j2
public class ClientLoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private static void traceRequest(HttpRequest request) {
        log.debug("Calling http method={} url={} headers={}", request::getMethod, request::getURI, request::getHeaders);
    }


    private static void traceResponse(ClientHttpResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("Response httpStatus={}, statusText={}, headers={}", () -> Try.of(response::getStatusCode).get(), () -> Try.of(response::getStatusText).get(), response::getHeaders);
        }
    }
}
