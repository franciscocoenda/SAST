package es.technest.security.api.config;

import es.technest.security.api.security.signature.SignatureProvider;
import es.technest.security.api.security.signature.SignatureVerifierRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String PING_ENDPOINT_PATTERN = "/ping";
    private static final String CLIENT_ACTUATOR_ENDPOINT_PATTERN = "/actuator/clients";

    @Autowired
    private SignatureProvider signatureProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludedEndpoints = new ArrayList<>();
        excludedEndpoints.add(PING_ENDPOINT_PATTERN);
        excludedEndpoints.add(CLIENT_ACTUATOR_ENDPOINT_PATTERN);

        registry.addInterceptor(
                new SignatureVerifierRequestInterceptor(signatureProvider))
                .excludePathPatterns(excludedEndpoints);
    }
}
