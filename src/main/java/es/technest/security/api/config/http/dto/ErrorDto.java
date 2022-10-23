package es.technest.security.api.config.http.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorDto {

    String error;
    ErrorCode code;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorDto(String error, ErrorCode code) {
        this.error = error;
        this.code = code;
    }

    public ErrorDto(String error) {
        this.error = error;
        this.code = null;
    }
}
