package es.technest.security.api.resource.dto.validators;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UrlIsHttpHostConstrainValidator implements ConstraintValidator<UrlIsHttpHostConstrain, URL> {

    private static final Collection<String> ACCEPTED_PROTOCOLS = List.of("http", "https");

    @Override
    public void initialize(UrlIsHttpHostConstrain urlIsHttpHostConstrain) {
    }

    @Override
    public boolean isValid(URL value, ConstraintValidatorContext context) {
        return !Objects.isNull(value) &&
                ACCEPTED_PROTOCOLS.contains(value.getProtocol().toLowerCase()) &&
                StringUtils.isNotEmpty(value.getHost());
    }
}
