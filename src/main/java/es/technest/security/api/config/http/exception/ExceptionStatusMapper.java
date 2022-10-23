package es.technest.security.api.config.http.exception;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ExceptionStatusMapper {

    public static int statusCodeFrom(RuntimeException ex) {
        if(ex instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN.value();
        }
        if (ex instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (ex instanceof ConflictException) {
            return HttpStatus.CONFLICT.value();
        }
        if (ex instanceof InternalServerErrorException) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND.value();
        }
        if (ex instanceof RequestTimeoutException) {
            return HttpStatus.GATEWAY_TIMEOUT.value();
        }
        if (ex instanceof UnprocessableEntityException) {
            return HttpStatus.UNPROCESSABLE_ENTITY.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
