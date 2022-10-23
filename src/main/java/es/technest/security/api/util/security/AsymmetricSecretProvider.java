package es.technest.security.api.util.security;

import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.stream.IntStream;

@Component
public class AsymmetricSecretProvider implements SecretProvider {

    private static final int MAX_BYTE_NUMBER_OF_A_UTF_8_CHAR = 4;
    private static final int NUMBER_OF_ACCEPTABLE_CHARS = 94;
    private static final int FIRST_ACCEPTABLE_CHAR_CODE = 33;

    // from 33 -> 126 decimal to convert to char are the printable, acceptable ascii characters for us.
    private char[] acceptableAsciiChars;

    private SecureRandom secureRandom;

    public AsymmetricSecretProvider() {
        this.acceptableAsciiChars = new char[NUMBER_OF_ACCEPTABLE_CHARS];
        IntStream.range(0, NUMBER_OF_ACCEPTABLE_CHARS).forEach(i -> acceptableAsciiChars[i] = (char)(i + FIRST_ACCEPTABLE_CHAR_CODE));
        this.secureRandom = new SecureRandom();
    }

    public String getSecureRandomString(int generatedStringLengthInUTF8Encoding) {
        // this should be limited for the acceptable length.
        final int lengthForStringGeneration = Integer.min(generatedStringLengthInUTF8Encoding, MAX_ASYMMETRIC_ENCRYPTABLE_LENGTH);
        StringBuilder sb = new StringBuilder();

        return Try.of(() -> {
            while(sb.toString().getBytes(StandardCharsets.UTF_8.name()).length <= lengthForStringGeneration - MAX_BYTE_NUMBER_OF_A_UTF_8_CHAR) {
                sb.append(acceptableAsciiChars[secureRandom.nextInt(acceptableAsciiChars.length)]);
            }
            return sb.toString();
        }).get();
    }
}
