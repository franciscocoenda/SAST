package es.technest.security.api.util;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

import static es.technest.security.api.util.CorrelationId.clearCorrelationId;
import static es.technest.security.api.util.CorrelationId.setCorrelationId;

@UtilityClass
public class CorrelationIdFunctions {

    public static void withCorrelationId(String correlationId, Runnable runnable) {
        try {
            setCorrelationId(correlationId);
            runnable.run();
        } finally {
            clearCorrelationId();
        }
    }

    public static <V> V withCorrelationId(String correlationId, Supplier<V> supplier) {
        try {
            setCorrelationId(correlationId);
            return supplier.get();
        } finally {
            clearCorrelationId();
        }
    }
}

