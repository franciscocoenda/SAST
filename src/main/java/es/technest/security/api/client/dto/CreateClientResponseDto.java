package es.technest.security.api.client.dto;

import lombok.Value;

@Value
public class CreateClientResponseDto {

    String key;

    String encryptedSecretBase64Encoded;

}
