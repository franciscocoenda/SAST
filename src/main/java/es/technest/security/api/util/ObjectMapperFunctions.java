package es.technest.security.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class ObjectMapperFunctions {

    public static <T> Optional<JsonNode> objectToJsonNode(ObjectMapper objectMapper, T value) {

        Try<JsonNode> as = Try.of(() -> objectMapper.readTree(objectMapper.writeValueAsString(value)));

        JsonNode aas = as.getOrElse(() -> null);
        return Optional.ofNullable(aas);
    }

}
