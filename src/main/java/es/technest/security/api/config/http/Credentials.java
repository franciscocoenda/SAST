package es.technest.security.api.config.http;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
public class Credentials {

    String username;

    String password;
}
