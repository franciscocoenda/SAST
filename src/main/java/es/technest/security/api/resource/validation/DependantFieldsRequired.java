package es.technest.security.api.resource.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DependantFieldsRequiredValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DependantFieldsRequired {

    String message() default "A list of co-dependant properties has some present and missing values.";

    String[] fields();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
