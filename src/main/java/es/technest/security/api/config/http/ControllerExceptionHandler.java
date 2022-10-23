package es.technest.security.api.config.http;

import es.technest.security.api.config.http.dto.ErrorDto;
import es.technest.security.api.config.http.exception.BadGatewayException;
import es.technest.security.api.config.http.exception.BadRequestException;
import es.technest.security.api.config.http.exception.ConflictException;
import es.technest.security.api.config.http.exception.InternalServerErrorException;
import es.technest.security.api.config.http.exception.NotFoundException;
import es.technest.security.api.config.http.exception.RequestTimeoutException;
import es.technest.security.api.config.http.exception.UnauthorizedException;
import es.technest.security.api.config.http.exception.UnprocessableEntityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@Log4j2
@RestControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDto unknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage());
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDto unknownException(InternalServerErrorException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDto handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDto handleConflictException(ConflictException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDto handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(RequestTimeoutException.class)
    @ResponseStatus(REQUEST_TIMEOUT)
    public ErrorDto handleRequestTimeoutException(RequestTimeoutException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ErrorDto handleException(HttpMessageConversionException exception) {
        return logWarningAndBuildErrorDtoWithMessage(exception, "Unable to convert the request: " + exception.getMostSpecificCause().getMessage() );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorDto handleException(BindException exception) {
        List<String> errors = retrieveBindingErrors(exception.getBindingResult());
        return logWarningAndBuildErrorDto(exception, errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleException(MethodArgumentNotValidException exception) {
        List<String> errors = retrieveBindingErrors(exception.getBindingResult());
        return logWarningAndBuildErrorDto(exception, errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDto handleException(MethodArgumentTypeMismatchException exception) {
        String message = buildMessageForTypeMismatch(exception.getName(), exception.getRequiredType());
        return logWarningAndBuildErrorDto(exception, message);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableEntityException.class)
    public ErrorDto handleException(UnprocessableEntityException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDto(exception.getMessage(), exception.getErrorCode());
    }

    @ResponseStatus(BAD_GATEWAY)
    @ExceptionHandler(BadGatewayException.class)
    public ErrorDto handleException(BadGatewayException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorDto handleException(UnauthorizedException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDto(exception.getMessage(), exception.getErrorCode());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorDto methodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage());
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    public ErrorDto unsupportedMediaTypeStatusException(HttpMediaTypeNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage());
    }


    private List<String> retrieveBindingErrors(Errors errors) {
        return errors.getAllErrors().stream().map(this::retrieveMessageFromBindingError).collect(toList());
    }

    private String buildMessageForTypeMismatch(String argumentName, Class<?> requiredType) {
        if (requiredType.isEnum()) {
            String validValues = Arrays.stream(requiredType.getEnumConstants()).map(Object::toString).collect(joining(","));
            return "The parameter " + argumentName + " must have a value among : " + validValues;
        }
        return "The parameter " + argumentName + " must be of type " + requiredType.getTypeName();
    }

    private String retrieveMessageFromBindingError(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + " " + error.getDefaultMessage();
        }
        return error.getObjectName() + " " + error.getDefaultMessage();
    }


    private ErrorDto logWarningAndBuildErrorDto(Throwable exception, List<String> errors) {
        log.warn(exception.getMessage());
        return new ErrorDto(errors.stream().collect(joining(",")));
    }

    private ErrorDto logWarningAndBuildErrorDto(Throwable exception, String message) {
        log.warn(exception.getMessage());
        return new ErrorDto(message);
    }

    private ErrorDto logWarningAndBuildErrorDtoWithMessage(Throwable exception, String message) {
        log.warn(exception.getMessage());
        return new ErrorDto(message);
    }
}
