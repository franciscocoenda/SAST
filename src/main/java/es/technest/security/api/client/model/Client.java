package es.technest.security.api.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static lombok.AccessLevel.PRIVATE;

@Document(collection = "client")
@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Client {

    @Id
    ClientId clientId;

    String clientKey;

    ClientParameters parameters;
}
