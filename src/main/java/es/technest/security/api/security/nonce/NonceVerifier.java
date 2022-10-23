package es.technest.security.api.security.nonce;

import es.technest.security.api.client.ClientRepository;
import es.technest.security.api.client.model.ClientParameters;
import es.technest.security.api.client.model.NonceConfiguration;
import es.technest.security.api.security.nonce.exception.NonceIsNotValidException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Log4j2
@Component
public class NonceVerifier {

    private static final Long NO_WINDOW = 0L;

    private ClientRepository clientRepository;

    private NonceRepository nonceRepository;

    @Autowired
    NonceVerifier(ClientRepository clientRepository,
                  NonceRepository nonceRepository) {
        this.clientRepository = clientRepository;
        this.nonceRepository = nonceRepository;
    }

    public boolean verifyOrThrow(String clientKey, long nonce) {
        val client = clientRepository.findClientByClientKeyOrThrow(clientKey);
        val clientId = client.getClientId();
        val window = ofNullable(client.getParameters()).map(ClientParameters::getNonceConfiguration).map(NonceConfiguration::getNonceWindow).orElse(NO_WINDOW);
        boolean tryAgain;
        boolean isValid = false;
        do {
            try {
                tryAgain = false;
                isValid = nonceRepository.update(clientId, nonce, window);
            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                tryAgain = true;
                log.info("OptimisticLockingFailureException occurred during one of the request with clientId={}", clientId.getId());
            }
        } while (tryAgain);

        if (!isValid) {
            throw new NonceIsNotValidException(clientId, nonce);
        }

        return true;
    }
}
