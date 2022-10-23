package es.technest.security.api.security.nonce;

import es.technest.security.api.security.nonce.model.Nonce;
import es.technest.security.api.security.nonce.model.NonceId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonceRepository extends CrudRepository<Nonce, NonceId>, NonceRepositoryExtended {

    String NONCE_COLLECTION_NAME = "nonce";
}