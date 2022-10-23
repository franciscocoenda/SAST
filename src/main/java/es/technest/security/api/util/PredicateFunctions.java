package es.technest.security.api.util;

import lombok.experimental.UtilityClass;

import java.util.function.Predicate;

@UtilityClass
public class PredicateFunctions {

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

}
