package es.technest.security.api.client;

import es.technest.security.api.client.dto.CreateClientRequestDto;
import es.technest.security.api.client.dto.CreateClientResponseDto;
import es.technest.security.api.client.model.CreateClientRequest;
import es.technest.security.api.client.model.CreateClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Component
@RestControllerEndpoint(id = "clients")
public class ClientActuatorEndpoint {

    private final ClientService clientService;

    @Autowired
    public ClientActuatorEndpoint(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateClientResponseDto createClient(@RequestBody @Valid CreateClientRequestDto createClientRequestDto) {
        CreateClientRequest clientRequestDtoToModel = ClientMapper.createClientRequestDtoToModel(createClientRequestDto);
        CreateClientResponse createClientResponse = clientService.createClient(clientRequestDtoToModel);
        return ClientMapper.createClientResponseToDto(createClientResponse);
    }
}