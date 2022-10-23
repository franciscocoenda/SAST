package es.technest.security.api.security.nonce;

import es.technest.security.api.client.model.ClientId;
import org.springframework.stereotype.Repository;

@Repository
public interface NonceRepositoryExtended {
    boolean update(ClientId clientId, Long nonce, Long window);
}