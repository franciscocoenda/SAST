package es.technest.security.api.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Consumer;

@UtilityClass
public class ConsumerFunctions {


    public static <T> Consumer<T> doNothing() {
        return x -> {
        };
    }

    public static void doNothing(Object objectConsumedToIgnore) {
    }

    public static <T> void applyConsumerIfNotNull(Consumer<T> originalConsumer, T valueToConsume) {
        if (Objects.isNull(valueToConsume)) {
            return;
        }

        originalConsumer.accept(valueToConsume);
    }
}
