package es.technest.security.api.security.signature;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class SignatureHeaderProvider {

    static final String SIGNATURE_HEADER = "signature";

    public static HttpHeaders headersWithSignature(String signature) {
        HttpHeaders withSignature = new HttpHeaders();
        withSignature.add(SIGNATURE_HEADER, signature);
        return withSignature;
    }
}
