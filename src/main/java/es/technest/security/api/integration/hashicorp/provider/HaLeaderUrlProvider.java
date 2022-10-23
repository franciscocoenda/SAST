package es.technest.security.api.integration.hashicorp.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.technest.security.api.integration.hashicorp.config.VaultConfig;
import es.technest.security.api.integration.hashicorp.dto.HaStatusResponseDto;
import es.technest.security.api.integration.hashicorp.exception.InternalErrorRetrievingLeaderUrlException;
import es.technest.security.api.integration.hashicorp.exception.LeaderProviderServerGatewayErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

import static es.technest.security.api.config.http.RestTemplateFunctions.getForStringEntityWithHeaders;
import static es.technest.security.api.config.http.RestTemplateFunctions.responseToClassOrThrow;

@Component
public class HaLeaderUrlProvider implements Supplier<String> {

    private final RestTemplate haProviderRestTemplate;

    private final ObjectMapper objectMapper;

    private final VaultConfig vaultConfig;

    public HaLeaderUrlProvider(RestTemplate haProviderRestTemplate,
                               ObjectMapper objectMapper,
                               VaultConfig vaultConfig) {
        this.haProviderRestTemplate = haProviderRestTemplate;
        this.objectMapper = objectMapper;
        this.vaultConfig = vaultConfig;
    }

    public String get() {
        HaStatusResponseDto haStatusDto = getHashiCorpVaultHaStatus();

        if(haStatusDto.isHaEnabled()) {
            return haStatusDto.getLeaderAddress();
        }

        return vaultConfig.getLeaderBaseUrl();
    }

    private HaStatusResponseDto getHashiCorpVaultHaStatus() {
        try {
            ResponseEntity<String> response = getForStringEntityWithHeaders(
                    haProviderRestTemplate,
                    vaultConfig.getUrlForHighAvailabilityStatus(),
                    new HttpHeaders());

            return responseToClassOrThrow(objectMapper, response, HaStatusResponseDto.class);
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new LeaderProviderServerGatewayErrorException(e);
        } catch (RuntimeException e) {
            throw new InternalErrorRetrievingLeaderUrlException(e);
        }
    }
}

