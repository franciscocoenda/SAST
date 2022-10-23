package es.technest.security.api.util;

import io.vavr.control.Either;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@UtilityClass
public class ExecuteSafelyFunctions {

    public static <T> Either<RuntimeException, T> executeSafelyToEither(Supplier<T> function) {
        Objects.requireNonNull(function, "function cannot be null");
        try {
            return right(function.get());
        } catch (RuntimeException exception) {
            return left(exception);
        }
    }

    public static <T> Consumer<T> makeConsumerSafelyOrErrorConsumer(Consumer<T> original, Consumer<RuntimeException> errorConsumer) {
        return t -> {
            try {
                original.accept(t);
            } catch (RuntimeException exception) {
                errorConsumer.accept(exception);
            }
        };
    }
}
