package es.technest.security.api.config.http;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class HttpRequestLogging {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);

        filter.setIncludePayload(false); // avoid exposure of sensitive information in the payload

        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        filter.setAfterMessagePrefix("AFTER REQUEST DATA: ");
        filter.setBeforeMessagePrefix("BEFORE REQUEST DATA: ");
        return filter;
    }

}
