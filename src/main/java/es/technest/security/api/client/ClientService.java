package es.technest.security.api.client;

import es.technest.security.api.client.exception.ClientNotFoundException;
import es.technest.security.api.client.model.Client;
import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.client.model.CreateClientRequest;
import es.technest.security.api.client.model.CreateClientResponse;
import es.technest.security.api.integration.hashicorp.HashiCorpClient;
import es.technest.security.api.integration.hashicorp.model.Token;
import es.technest.security.api.security.nonce.NonceRepository;
import es.technest.security.api.security.nonce.model.Nonce;
import es.technest.security.api.security.nonce.model.NonceId;
import es.technest.security.api.security.signature.ClientSecretTokenRepository;
import es.technest.security.api.security.signature.model.SignatureSecret;
import es.technest.security.api.security.signature.model.SignatureSecretToken;
import es.technest.security.api.util.security.CryptographyHelper;
import es.technest.security.api.util.security.SecretProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static es.technest.security.api.util.security.SecretProvider.MAX_ASYMMETRIC_ENCRYPTABLE_LENGTH;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptySet;
import static java.util.UUID.randomUUID;

@Service
@Log4j2
public class ClientService {

    private static final int MAX_SECRET_LENGTH_BEFORE_BASE64_ENCODING = (MAX_ASYMMETRIC_ENCRYPTABLE_LENGTH / 4) * 3;

    private final ClientRepository clientRepository;

    private final ClientSecretTokenRepository clientSecretTokenRepository;

    private final NonceRepository nonceRepository;

    private final HashiCorpClient hashiCorpClient;

    private final CryptographyHelper cryptographyHelper;

    private final SecretProvider secretProvider;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         ClientSecretTokenRepository clientSecretTokenRepository,
                         NonceRepository nonceRepository,
                         HashiCorpClient hashiCorpClient,
                         CryptographyHelper cryptographyHelper,
                         SecretProvider secretProvider) {
        this.clientRepository = clientRepository;
        this.clientSecretTokenRepository = clientSecretTokenRepository;
        this.nonceRepository = nonceRepository;
        this.hashiCorpClient = hashiCorpClient;
        this.cryptographyHelper = cryptographyHelper;
        this.secretProvider = secretProvider;
    }

    public CreateClientResponse createClient(CreateClientRequest createClientRequest) {
        log.info("Creating new client");
        Client client = clientRepository.save(buildClient(createClientRequest));
        nonceRepository.save(buildClientNonce(client));
        String base64RandomSecret = getBase64RandomSecret();
        storeClientSecret(client, base64RandomSecret);
        log.info("Created new client with clientId={}", client.getClientId());
        return new CreateClientResponse(client.getClientKey(), encrypt(base64RandomSecret, createClientRequest.getEncryptionKey()));
    }

    private String getBase64RandomSecret() {
        String secureRandomString = secretProvider.getSecureRandomString(MAX_SECRET_LENGTH_BEFORE_BASE64_ENCODING);
        return Base64.getEncoder().encodeToString(secureRandomString.getBytes(UTF_8));
    }

    private void storeClientSecret(Client client, String secret) {
        Token secretToken = hashiCorpClient.save(new SignatureSecret(secret));
        Token encryptedToken = new Token(cryptographyHelper.encryptWithMasterKey(secretToken.getValue()));
        clientSecretTokenRepository.save(new SignatureSecretToken(client.getClientId(), encryptedToken));
    }

    private String encrypt(String secret, String encryptionKey) {
        return cryptographyHelper.asymmetricEncryptText(secret, encryptionKey);
    }

    private static Client buildClient(CreateClientRequest createClientRequest) {
        return new Client(ClientId.valueOf(randomUUID()),
                randomUUID().toString(),
                createClientRequest.getClientParameters());
    }

    private static Nonce buildClientNonce(Client client) {
        return new Nonce(NonceId.valueOf(randomUUID()), client.getClientId(), null, 0L, emptySet());
    }

    private Client findClientByClientKeyOrThrow(String clientKey) {
        return clientRepository.findByClientKey(clientKey).orElseThrow(() -> new ClientNotFoundException(clientKey));
    }
}
