package es.technest.security.api.client;

import es.technest.security.api.client.exception.ClientNotFoundException;
import es.technest.security.api.client.model.Client;
import es.technest.security.api.client.model.ClientId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, ClientId> {

    Optional<Client> findByClientKey(String clientKey);

    default Client findClientByClientKeyOrThrow(String clientKey) {
        return this.findByClientKey(clientKey).orElseThrow(() -> new ClientNotFoundException(clientKey));
    }

    default Client findClientByIdOrThrow(ClientId clientId) {
        return this.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));
    }
}
