package es.technest.security.api.security.signature;

import es.technest.security.api.security.signature.exception.HmacSignatureValidationException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static es.technest.security.api.security.signature.SignatureHeaderProvider.SIGNATURE_HEADER;

@Log4j2
@Component
public class SignatureVerifierRequestInterceptor extends HandlerInterceptorAdapter {

    private static final String CLIENT_KEY_HEADER = "key";

    private final SignatureProvider signatureProvider;

    @Autowired
    public SignatureVerifierRequestInterceptor(SignatureProvider signatureProvider) {
        this.signatureProvider = signatureProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        val signatureFromHeader = request.getHeader(SIGNATURE_HEADER);
        if (signatureFromHeader == null) {
            throw new HmacSignatureValidationException();
        }

        val clientKey = request.getHeader(CLIENT_KEY_HEADER);

        val requestURI = request.getRequestURI();
        val payload = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        val generatedSignature = signatureProvider.generateSignature(requestURI, clientKey, payload);

        if (signatureFromHeader.equals(generatedSignature)) {
            return true;
        }

        throw new HmacSignatureValidationException(clientKey);
    }
}