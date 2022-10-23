package es.technest.security.api.config.http;

import es.technest.security.api.util.CorrelationId;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import static es.technest.security.api.util.CorrelationId.getCorrelationId;

public class CorrelationIdHeaderInterceptor {

    public static ClientHttpRequestInterceptor correlationIdHeaderInterceptor() {
        return (request, body, execution) -> {
            String currentCorrelationId = getCorrelationId();
            if (currentCorrelationId != null) {
                request.getHeaders().set(CorrelationId.CORRELATION_ID_HEADER, currentCorrelationId);
            }
            return execution.execute(request, body);
        };
    }
}
