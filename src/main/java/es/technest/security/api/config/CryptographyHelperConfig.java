package es.technest.security.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cryptography")
public class CryptographyHelperConfig {

    private String asymmetricAlgorithm;
    private String symmetricAlgorithm;
    private Integer keySize;
    private String pspMasterKeySalt;
    private String pspMasterKeyMainKey;
    private Integer cipherPoolMinIdle;
    private Integer cipherPoolMaxIdle;
}
