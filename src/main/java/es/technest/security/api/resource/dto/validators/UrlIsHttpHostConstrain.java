package es.technest.security.api.resource.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UrlIsHttpHostConstrainValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlIsHttpHostConstrain {

    String message() default "Invalid URL, it must contains http or https as protocol and must contains a host";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
