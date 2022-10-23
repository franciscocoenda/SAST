package es.technest.security.api.security.nonce.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

import java.util.UUID;

@Value
public class NonceId {

    UUID id;

    private NonceId(UUID id) {
        this.id = id;
    }

    public static NonceId valueOf(String nonceId) {
        return new NonceId(UUID.fromString(nonceId));
    }

    public static NonceId valueOf(UUID nonceId) {
        return new NonceId(nonceId);
    }

    @JsonValue
    public String toValue() {
        return id.toString();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}