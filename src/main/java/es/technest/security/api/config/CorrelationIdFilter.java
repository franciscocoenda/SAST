package es.technest.security.api.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static es.technest.security.api.util.CorrelationId.CORRELATION_ID_HEADER;

public class CorrelationIdFilter implements Filter {

    private final CorrelationId correlationId;

    public CorrelationIdFilter(CorrelationId correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            String correlationIdValue = extractCorrelationIdFromRequestHeaderOrGenerateNew(httpServletRequest);
            correlationId.set(correlationIdValue);
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse) response).addHeader(CORRELATION_ID_HEADER, correlationIdValue);
            }
            chain.doFilter(httpServletRequest, response);
        } finally {
            correlationId.clear();
        }
    }

    private String extractCorrelationIdFromRequestHeaderOrGenerateNew(HttpServletRequest httpServletRequest) {
        String correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        return correlationId;
    }

    @Override
    public void destroy() {
        correlationId.clear();
    }
}