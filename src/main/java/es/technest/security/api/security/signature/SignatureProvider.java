package es.technest.security.api.security.signature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.technest.security.api.client.ClientRepository;
import es.technest.security.api.client.exception.ClientNotFoundException;
import es.technest.security.api.client.model.Client;
import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.security.signature.exception.UnableToConvertObjectToJsonStringException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Log4j2
@Component
public class SignatureProvider {

    private final ClientRepository clientRepository;

    private final ObjectMapper objectMapper;

    private final ClientSecretProvider clientSecretProvider;

    @Autowired
    SignatureProvider(ClientRepository clientRepository,
                      ObjectMapper objectMapper,
                      ClientSecretProvider clientSecretProvider) {
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
        this.clientSecretProvider = clientSecretProvider;
    }

    private static String EMPTY_URL_STRING = "";

    public String generateSignatureFromObject(String clientKey, Object data) {
        return generateSignatureClientId(EMPTY_URL_STRING, clientKey, asJson(data));
    }

    public String generateSignature(ClientId clientId, String data) {
        return generateSignatureWithClientSecret(EMPTY_URL_STRING, data, clientId);
    }

    public String generateSignature(String url, String clientKey, String data) {
        return generateSignatureClientId(url, clientKey, data);
    }

    private String generateSignatureClientId(String url, String clientKey, String data) {
        val clientId = findClientByClientKeyOrThrow(clientKey).getClientId();
        return generateSignatureWithClientSecret(url, data, clientId);
    }

    private String generateSignatureWithClientSecret(String url, String postData, ClientId clientId) {
        log.debug("url={}", url);
        log.debug("postData={}", postData);

        String secretBase64Encoded = clientSecretProvider.getClientSecret(clientId).getValue();
        String sha256Hex = DigestUtils.sha256Hex(postData);
        log.debug("sha256Hex={}", sha256Hex);

        val computedSignature = url + sha256Hex;
        log.debug("computedSignature={}", computedSignature);

        val keyInBytes = Base64.getDecoder().decode(secretBase64Encoded);

        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, keyInBytes).hmacHex(computedSignature);
    }

    private Client findClientByClientKeyOrThrow(String clientKey) {
        return clientRepository.findByClientKey(clientKey).orElseThrow(() -> new ClientNotFoundException(clientKey));
    }

    private String asJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Unable to convert " + object.getClass().getSimpleName() + " to String: ", ex);
            throw new UnableToConvertObjectToJsonStringException(object);

        }
    }
}
