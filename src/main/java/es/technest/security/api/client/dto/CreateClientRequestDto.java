package es.technest.security.api.client.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Value
public class CreateClientRequestDto {

    @NotEmpty
    String encryptionKeyBase64Encoded;

    @Min(0)
    Long nonceWindow;
}
