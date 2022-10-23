package es.technest.security.api.config;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;

import java.util.UUID;

@UtilityClass
@Log4j2
public class CorrelationId {

    public static final String CORRELATION_ID = "correlationId";
    public static final String CORRELATION_ID_HEADER = "x-correlation-id";

    public static void set(String correlationId) {
        String current = MDC.get(CORRELATION_ID);
        if (current != null) {
            log.warn("Overriding correlation id with correlationId={}", correlationId);
        }
        MDC.put(CORRELATION_ID, correlationId);
        log.debug("Set correlationId={}", correlationId);
    }

    public static String get() {
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

    public static void clear() {
        String correlationId = get();
        MDC.remove(CORRELATION_ID);
        log.debug("Cleared correlationId={}", correlationId);
    }
}
