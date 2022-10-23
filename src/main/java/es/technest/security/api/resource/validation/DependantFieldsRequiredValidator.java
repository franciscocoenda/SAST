package es.technest.security.api.resource.validation;

import lombok.val;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class DependantFieldsRequiredValidator implements ConstraintValidator<DependantFieldsRequired, Object> {

    private String[] dependantFields;

    @Override
    public void initialize(DependantFieldsRequired dependantFieldsRequiredAnnotation) {
        dependantFields = dependantFieldsRequiredAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        val dependantFieldsValues = Arrays.stream(this.dependantFields).map(field -> new BeanWrapperImpl(value).getPropertyValue(field)).collect(toList());
        val isValid = dependantFieldsValues.stream().allMatch(Objects::isNull) || dependantFieldsValues.stream().allMatch(Objects::nonNull);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    format("Properties '%s' must be all set or all null", Arrays.toString(this.dependantFields)))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
