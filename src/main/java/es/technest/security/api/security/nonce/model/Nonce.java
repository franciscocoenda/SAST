package es.technest.security.api.security.nonce.model;

import es.technest.security.api.client.model.ClientId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

import static es.technest.security.api.security.nonce.NonceRepository.NONCE_COLLECTION_NAME;
import static lombok.AccessLevel.PRIVATE;

@Document(collection = NONCE_COLLECTION_NAME)
@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Nonce {

    @Id
    NonceId nonceId;

    @NotNull
    ClientId clientId;

    @Version
    Long version;

    @NotNull
    Long maxNonce;

    Set<Long> usedNonces;
}
