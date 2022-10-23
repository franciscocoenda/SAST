package es.technest.security.api.client;

import es.technest.security.api.client.dto.CreateClientRequestDto;
import es.technest.security.api.client.dto.CreateClientResponseDto;
import es.technest.security.api.client.model.ClientParameters;
import es.technest.security.api.client.model.CreateClientRequest;
import es.technest.security.api.client.model.CreateClientResponse;
import es.technest.security.api.client.model.NonceConfiguration;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientMapper {

    public static CreateClientRequest createClientRequestDtoToModel(CreateClientRequestDto createClientRequestDto) {
        return new CreateClientRequest(
                createClientRequestDto.getEncryptionKeyBase64Encoded(),
                new ClientParameters(
                        new NonceConfiguration(createClientRequestDto.getNonceWindow())
                )
        );
    }

    public static CreateClientResponseDto createClientResponseToDto(CreateClientResponse createClientResponse) {
        return new CreateClientResponseDto(createClientResponse.getClientKey(), createClientResponse.getEncryptedClientSecret());
    }

}
