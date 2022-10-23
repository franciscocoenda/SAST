package es.technest.security.api.integration;

import es.technest.security.api.integration.hashicorp.VaultHashiCorpClient;
import lombok.val;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

@Component
public class IntegrationHealthIndicator extends AbstractHealthIndicator {

    private final VaultHashiCorpClient vaultHashiCorpClient;

    public IntegrationHealthIndicator(VaultHashiCorpClient vaultHashiCorpClient) {
        this.vaultHashiCorpClient = vaultHashiCorpClient;
    }

    @Override
    protected void doHealthCheck(Builder builder) {
        val overallIntegrationHealthState = Stream.of(
                setAndRetrieveHealthState(builder, vaultHashiCorpClient, "HashiCorp Vault Service"))
                .reduce(TRUE, (a, b) -> a && b);

        setOverallIntegrationHealthState(builder, overallIntegrationHealthState);
    }

    private boolean setAndRetrieveHealthState(Builder builder, HealthAware healthAware, String nameForTheDetail) {
        boolean isHealthy = healthAware.isHealthy();
        builder.withDetail(nameForTheDetail, isHealthy ? UP : DOWN);
        return isHealthy;
    }

    private void setOverallIntegrationHealthState(Builder builder, boolean overallSystemHealthy) {
        if (overallSystemHealthy) {
            builder.status(UP);
        } else {
            builder.status(DOWN);
        }
    }
}
