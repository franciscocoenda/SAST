package es.technest.security.api.integration.hashicorp.config;

import es.technest.security.api.config.http.ClientLoggingRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static es.technest.security.api.config.http.CorrelationIdHeaderInterceptor.correlationIdHeaderInterceptor;
import static java.time.temporal.ChronoUnit.MILLIS;

@Configuration
public class VaultRestConfig {

    @Bean
    public RestTemplate vaultRestTemplate(RestTemplateBuilder restTemplateBuilder, ClientHttpRequestFactory vaultClientHttpRequestFactory, VaultConfig vaultConfig) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.of(vaultConfig.getRestTemplate().getConnectTimeout(), MILLIS))
                .setReadTimeout(Duration.of(vaultConfig.getRestTemplate().getReadTimeout(), MILLIS))
                .interceptors(new ClientLoggingRequestInterceptor(), correlationIdHeaderInterceptor())
                .requestFactory(() -> vaultClientHttpRequestFactory)
                .build();
    }

    @Bean
    public ClientHttpRequestFactory vaultClientHttpRequestFactory(VaultConfig vaultConfig) {
        return new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient(vaultConfig)));
    }

    private HttpClient httpClient(VaultConfig vaultConfig) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setMaxConnTotal(vaultConfig.getHttp().getMaxConnections())
                .setMaxConnPerRoute(vaultConfig.getHttp().getMaxConnectionsPerRoute());

        return httpClientBuilder.build();
    }
}
