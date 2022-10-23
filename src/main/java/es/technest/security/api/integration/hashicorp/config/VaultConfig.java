package es.technest.security.api.integration.hashicorp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "hashi-corp-vault")
public class VaultConfig {
    private String url;

    private String port;

    private Tokens tokens;

    private ManageCubbyhole manageCubbyhole;

    private ObtainTokenForCubbyhole obtainTokenForCubbyhole;

    private SettingsForCubbyhole settingsForCubbyhole;

    private ObtainForLeaderUrl obtainForLeaderUrl;

    private ObtainCapabilities obtainCapabilities;

    private RestTemplate restTemplate;

    private Http http;

    public String getLeaderBaseUrl() {
        return url + ":" + port;
    }

    public String getUrlForHighAvailabilityStatus() {
        return getLeaderBaseUrl() + obtainForLeaderUrl.getContextPath();
    }

    public String getUrlForManageCubbyHoles(String baseUrl) {
        return baseUrl + manageCubbyhole.getContextPath();
    }

    public String getUrlForObtainCubbyHoleToken(String baseUrl) {
        return baseUrl + obtainTokenForCubbyhole.getContextPath();
    }

    public String getUrlForObtainCapabilities(String baseUrl) {
        return baseUrl + obtainCapabilities.getContextPath();
    }

    @Getter
    @Setter
    public static class Http {
        private Integer maxConnections;
        private Integer maxConnectionsPerRoute;
    }

    @Getter
    @Setter
    public static class RestTemplate {
        private Integer connectTimeout;
        private Integer readTimeout;
    }

    @Getter
    @Setter
    public static class Tokens {
        private String clientToken;
        private String accessorToken;
    }

    @Getter
    @Setter
    public static class ManageCubbyhole {
        private String contextPath;
    }

    @Getter
    @Setter
    public static class ObtainTokenForCubbyhole {
        private String contextPath;
    }

    @Getter
    @Setter
    public static class ObtainForLeaderUrl {
        private String contextPath;
    }

    @Getter
    @Setter
    public static class SettingsForCubbyhole {
        private Integer ttl;
        private Integer numUses;
    }

    @Getter
    @Setter
    public static class ObtainCapabilities {
        private String contextPath;
    }
}
