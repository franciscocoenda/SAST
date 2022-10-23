package es.technest.security.api.util.security;

import es.technest.security.api.config.CryptographyHelperConfig;
import io.vavr.control.Try;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class CryptographyHelper {

    private CryptographyHelperConfig config;

    private SecretKeySpec treasureMasterKeySpec;
    private CipherObjectPool symmetricCipherPool;
    private CipherObjectPool asymmetricCipherPool;
    private KeyFactory asymmetricKeyFactory;

    @Autowired
    public CryptographyHelper(CryptographyHelperConfig config, CipherObjectPool symmetricCipherPool, CipherObjectPool asymmetricCipherPool) throws NoSuchAlgorithmException {
        this.config = config;
        this.symmetricCipherPool = symmetricCipherPool;
        this.asymmetricCipherPool = asymmetricCipherPool;
        this.asymmetricKeyFactory = KeyFactory.getInstance(config.getAsymmetricAlgorithm());
        this.treasureMasterKeySpec = getTreasureMasterKeySpec();
    }

    public String encryptWithMasterKey(String msg) {
        return symmetricEncryptText(msg, treasureMasterKeySpec);
    }

    public String decryptWithMasterKey(String msg) {
        return symmetricDecryptText(msg, treasureMasterKeySpec);
    }

    public String asymmetricEncryptText(String msg, Key key) {
        return executeWithCipher(asymmetricCipherPool, asymmetricCipher -> Try.of(() -> {
            asymmetricCipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String(asymmetricCipher.doFinal(msg.getBytes(UTF_8.name())));
        }).get());
    }

    public String asymmetricEncryptText(String msg, String publicKey) {
        return asymmetricEncryptText(msg, convertFromPublicKey(publicKey));
    }

    public String asymmetricDecryptText(String msg, Key key) {
        return executeWithCipher(asymmetricCipherPool, asymmetricCipher -> Try.of(() -> {
            asymmetricCipher.init(Cipher.DECRYPT_MODE, key);
            return new String(asymmetricCipher.doFinal(Base64.decodeBase64(msg)), UTF_8.name());
        }).get());
    }

    public String asymmetricDecryptText(String msg, byte[] privateKey) {
        return asymmetricDecryptText(msg, convertFromPrivateKey(privateKey));
    }

    public String symmetricEncryptText(String msg, SecretKeySpec keySpec) {
        return executeWithCipher(symmetricCipherPool, symmetricCipher -> Try.of(() -> {
            symmetricCipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] inputBytes = msg.getBytes();
            byte[] outputBytes = symmetricCipher.doFinal(inputBytes);

            return Base64Utils.encodeToString(outputBytes);
        }).get());
    }

    public String symmetricDecryptText(String msg, SecretKeySpec keySpec) {
        return executeWithCipher(symmetricCipherPool, symmetricCipher -> Try.of(() -> {
            symmetricCipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] dec = Base64Utils.decode(msg.getBytes());
            byte[] utf8 = symmetricCipher.doFinal(dec);

            return new String(utf8, UTF_8.name());
        }).get());
    }

    private PublicKey convertFromPublicKey(String publicKey) {
        return Try.of(() -> {
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            return asymmetricKeyFactory.generatePublic(X509publicKey);
        }).get();
    }

    private PrivateKey convertFromPrivateKey(byte[] privateKey) {
        return Try.of(() -> {
            KeyFactory keyFactory = KeyFactory.getInstance(config.getAsymmetricAlgorithm());
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateKey);
            return (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
        }).get();
    }

    private SecretKeySpec getTreasureMasterKeySpec() {
        return Try.of(() -> {
            byte[] key = (config.getPspMasterKeySalt() + config.getPspMasterKeyMainKey()).getBytes(UTF_8.name());
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            return new SecretKeySpec(key, config.getSymmetricAlgorithm());
        }).get();
    }

    public KeyPair generateKeyPair() {
        return Try.of(() -> {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(config.getAsymmetricAlgorithm());
            keyPairGenerator.initialize(config.getKeySize());
            return keyPairGenerator.genKeyPair();
        }).get();
    }

    String executeWithCipher(CipherObjectPool pool, Function<Cipher, String> function) {
        Cipher cipher = Try.of(pool::getTarget).getOrElseTry(() -> Cipher.getInstance(pool.getAlgorithmType()));
        return Try.of(() -> function.apply(cipher)).andFinally(() -> Try.run(() -> pool.releaseTarget(cipher))).get();
    }
}
