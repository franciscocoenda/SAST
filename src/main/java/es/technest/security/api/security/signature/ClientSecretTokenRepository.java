package es.technest.security.api.security.signature;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.security.signature.model.SignatureSecretToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientSecretTokenRepository extends CrudRepository<SignatureSecretToken, ClientId> {
}
