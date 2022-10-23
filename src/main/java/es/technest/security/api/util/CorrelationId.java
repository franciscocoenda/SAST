package es.technest.security.api.util;

import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
public class CorrelationId {

    public static final String CORRELATION_ID = "correlationId";
    public static final String CORRELATION_ID_HEADER = "x-correlation-id";

    public static void setCorrelationId(String correlationId) {
        String current = MDC.get(CORRELATION_ID);
        if (current != null) {
            log.warn("Overriding correlation id with correlationId={}", correlationId);
        }
        MDC.put(CORRELATION_ID, correlationId);
        log.debug("Set correlationId={}", correlationId);
    }

    public void set(String correlationId) {
        setCorrelationId(correlationId);
    }

    public static String getCorrelationId() {
        return getOrSetNew();
    }

    private static String getOrSetNew() {
        String correlationId = MDC.get(CORRELATION_ID);
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
            log.warn("Correlation Id was needed but not previously set. Setting new one correlationId={}", correlationId);
            MDC.put(CORRELATION_ID, correlationId);
        }
        return correlationId;
    }

    public static void clearCorrelationId() {
        String correlationId = MDC.get(CORRELATION_ID);
        log.debug("Clearing correlationId={}", correlationId);
        MDC.remove(CORRELATION_ID);
    }

    public void clear() {
        clearCorrelationId();
    }

}
