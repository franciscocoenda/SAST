package es.technest.security.api.security.signature;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.integration.hashicorp.HashiCorpClient;
import es.technest.security.api.integration.hashicorp.model.Token;
import es.technest.security.api.security.signature.exception.UnableToObtainClientSecretException;
import es.technest.security.api.security.signature.model.SignatureSecret;
import es.technest.security.api.security.signature.model.SignatureSecretToken;
import es.technest.security.api.util.security.CryptographyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component
public class ClientSecretProvider {

    private final ClientSecretTokenRepository clientSecretTokenRepository;
    private final HashiCorpClient hashiCorpClient;
    private final CryptographyHelper cryptographyHelper;

    @Autowired
    ClientSecretProvider(ClientSecretTokenRepository clientSecretTokenRepository, HashiCorpClient hashiCorpClient,
                         CryptographyHelper cryptographyHelper) {
        this.clientSecretTokenRepository = clientSecretTokenRepository;
        this.hashiCorpClient = hashiCorpClient;
        this.cryptographyHelper = cryptographyHelper;
    }

    @Cacheable("client-secrets")
    public SignatureSecret getClientSecret(ClientId clientId) {
        return clientSecretTokenRepository.findById(clientId)
                .map(SignatureSecretToken::getToken)
                .map(Token::getValue)
                .map(cryptographyHelper::decryptWithMasterKey)
                .map(Token::new)
                .map(token -> hashiCorpClient.retrieveData(token, SignatureSecret.class))
                .orElseThrow(() -> new UnableToObtainClientSecretException(clientId));
    }


}
