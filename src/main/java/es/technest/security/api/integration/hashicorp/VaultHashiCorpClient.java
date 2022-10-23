package es.technest.security.api.integration.hashicorp;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.technest.security.api.integration.hashicorp.config.VaultConfig;
import es.technest.security.api.integration.hashicorp.dto.CubbyHoleDataResponseDto;
import es.technest.security.api.integration.hashicorp.dto.CubbyHoleTokenResponseDto;
import es.technest.security.api.integration.hashicorp.exception.ExpiredTokenException;
import es.technest.security.api.integration.hashicorp.exception.InternalErrorRetrievingTokenDataException;
import es.technest.security.api.integration.hashicorp.exception.InternalErrorStoringTokenDataException;
import es.technest.security.api.integration.hashicorp.exception.TokenServerGatewayErrorException;
import es.technest.security.api.integration.hashicorp.model.Token;
import es.technest.security.api.integration.hashicorp.provider.HaLeaderUrlProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static es.technest.security.api.config.http.RestTemplateFunctions.getForStringEntityWithHeaders;
import static es.technest.security.api.config.http.RestTemplateFunctions.postForStringEntity;
import static es.technest.security.api.config.http.RestTemplateFunctions.responseToClassOrThrow;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Log4j2
public class VaultHashiCorpClient implements HashiCorpClient {

    private static final List<MediaType> ACCEPTS_APPLICATION_JSON = singletonList(APPLICATION_JSON);

    public static final String VAULT_TOKEN_HEADER = "x-vault-token";

    private final RestTemplate vaultRestTemplate;

    private final ObjectMapper objectMapper;

    private final VaultConfig vaultConfig;

    private final HaLeaderUrlProvider haLeaderUrlProvider;

    public VaultHashiCorpClient(RestTemplate vaultRestTemplate,
                                ObjectMapper objectMapper,
                                VaultConfig vaultConfig,
                                HaLeaderUrlProvider haLeaderUrlProvider) {
        this.vaultRestTemplate = vaultRestTemplate;
        this.objectMapper = objectMapper;
        this.vaultConfig = vaultConfig;
        this.haLeaderUrlProvider = haLeaderUrlProvider;
    }

    private static final Map<String, Object> BASE_CUBBYHOLE_TOKEN_REQUEST_MAP =
            Map.of("policies", singletonList("cubbyhole"),
                    "renewable", false,
                    "no_default_policy", true);

    @Override
    public <T> T retrieveData(Token token, Class<T> classType) {
        String leaderUrl = haLeaderUrlProvider.get();

        try {
            ResponseEntity<String> response = getForStringEntityWithHeaders(
                    vaultRestTemplate,
                    vaultConfig.getUrlForManageCubbyHoles(leaderUrl) + classType.getSimpleName(),
                    getHeaders(token.getValue()));

            Object storedData = responseToClassOrThrow(objectMapper, response, CubbyHoleDataResponseDto.class).getData();
            return objectMapper.convertValue(storedData, classType);

        } catch (HttpClientErrorException e) {
            if (HttpStatus.FORBIDDEN.equals(e.getStatusCode())) {
                throw new ExpiredTokenException(e);
            }
            throw new InternalErrorRetrievingTokenDataException(e);
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new TokenServerGatewayErrorException(e);
        } catch (RuntimeException e) {
            throw new InternalErrorRetrievingTokenDataException(e);
        }
    }

    @Override
    public <T> Token save(T data) {
        return save(data, vaultConfig.getSettingsForCubbyhole().getTtl());
    }

    @Override
    public <T> Token save(T data, Integer ttlInMinutes) {
        String leaderUrl = haLeaderUrlProvider.get();

        try {
            Objects.requireNonNull(data);

            Token token = retrieveToken(ttlInMinutes, leaderUrl);
            postForStringEntity(
                    vaultRestTemplate,
                    vaultConfig.getUrlForManageCubbyHoles(leaderUrl) + data.getClass().getSimpleName(),
                    data,
                    getHeaders(token.getValue())
            );
            return token;

        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new TokenServerGatewayErrorException(e);
        } catch (RuntimeException e) {
            throw new InternalErrorStoringTokenDataException(e);
        }
    }

    @Override
    public boolean isHealthy() {
        try {
            String leaderUrl = haLeaderUrlProvider.get();
            String token = vaultConfig.getTokens().getClientToken();

            postForStringEntity(
                    vaultRestTemplate,
                    vaultConfig.getUrlForObtainCapabilities(leaderUrl),
                    "{\"paths\" : [\"auth/token/create-orphan\"]}",
                    getHeaders(token)
            );
        } catch (RuntimeException ex) {
            log.error("Vault Service unavailable. Reason: " + ex.getLocalizedMessage(), ex);
            return false;
        }

        return true;
    }

    Token retrieveToken(Integer ttlInMinutes, String leaderUrl) {
        ResponseEntity<String> response =
                postForStringEntity(
                        vaultRestTemplate,
                        vaultConfig.getUrlForObtainCubbyHoleToken(leaderUrl),
                        getTokenRequest(ttlInMinutes),
                        getHeaders(vaultConfig.getTokens().getClientToken())
                );

        CubbyHoleTokenResponseDto token = responseToClassOrThrow(objectMapper, response, CubbyHoleTokenResponseDto.class);
        return new Token(token.getAuth().getClient_token());
    }

    private Map<String, Object> getTokenRequest(Integer ttlInMinutes) {
        return getTokenRequest(ttlInMinutes, vaultConfig.getSettingsForCubbyhole().getNumUses());
    }

    private static Map<String, Object> getTokenRequest(Integer ttlInMinutes, Integer num_uses) {
        Map<String, Object> requestMap = new HashMap<>(BASE_CUBBYHOLE_TOKEN_REQUEST_MAP);
        requestMap.put("ttl", ttlInMinutes + "m");
        requestMap.put("num_uses", num_uses);
        return requestMap;
    }

    private static HttpHeaders getHeaders(String token) {
        HttpHeaders vaultHeaders = new HttpHeaders();
        vaultHeaders.setAccept(ACCEPTS_APPLICATION_JSON);
        vaultHeaders.add(VAULT_TOKEN_HEADER, token);
        return vaultHeaders;
    }
}
