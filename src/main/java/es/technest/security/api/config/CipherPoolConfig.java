package es.technest.security.api.config;

import es.technest.security.api.util.security.CipherObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CipherPoolConfig {

    @Autowired
    private CryptographyHelperConfig config;

    @Bean
    @Scope("prototype")
    Cipher symmetricCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(config.getSymmetricAlgorithm());
    }

    @Bean
    @Scope("prototype")
    Cipher asymmetricCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(config.getAsymmetricAlgorithm());
    }

    @Bean
    CipherObjectPool symmetricCipherPool() {
        return new CipherObjectPool("symmetricCipher", config.getSymmetricAlgorithm(), config.getCipherPoolMinIdle(), config.getCipherPoolMaxIdle());
    }

    @Bean
    CipherObjectPool asymmetricCipherPool() {
        return new CipherObjectPool("asymmetricCipher", config.getAsymmetricAlgorithm(), config.getCipherPoolMinIdle(), config.getCipherPoolMaxIdle());
    }
}
