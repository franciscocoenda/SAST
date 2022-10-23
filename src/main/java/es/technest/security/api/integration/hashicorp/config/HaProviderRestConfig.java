package es.technest.security.api.integration.hashicorp.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static es.technest.security.api.config.http.CorrelationIdHeaderInterceptor.correlationIdHeaderInterceptor;
import static java.time.temporal.ChronoUnit.MILLIS;

@Configuration
public class HaProviderRestConfig {

    @Bean
    public RestTemplate haProviderRestTemplate(RestTemplateBuilder restTemplateBuilder, VaultConfig vaultConfig) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.of(vaultConfig.getRestTemplate().getConnectTimeout(), MILLIS))
                .setReadTimeout(Duration.of(vaultConfig.getRestTemplate().getReadTimeout(), MILLIS))
                .interceptors(correlationIdHeaderInterceptor())
                .build();
    }
}
