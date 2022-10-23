package es.technest.security.api.client.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

import java.util.UUID;

@Value
public class ClientId {

    UUID id;

    private ClientId(UUID id) {
        this.id = id;
    }

    public static ClientId valueOf(String accountId) {
        return new ClientId(UUID.fromString(accountId));
    }

    public static ClientId valueOf(UUID accountId) {
        return new ClientId(accountId);
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