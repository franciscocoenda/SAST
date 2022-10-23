package es.technest.security.api.resource;

import es.technest.security.api.resource.dto.CreateRequestDto;
import es.technest.security.api.resource.dto.CreateResponseDto;
import es.technest.security.api.resource.dto.FindFiltersDto;
import es.technest.security.api.resource.dto.FindResponseDto;
import es.technest.security.api.resource.dto.ResourceDto;
import es.technest.security.api.security.nonce.NonceVerifier;
import es.technest.security.api.security.signature.SignatureHeaderProvider;
import es.technest.security.api.security.signature.SignatureProvider;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

@Log4j2
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, path = "deposits")
public class SecuredResource {

    private final NonceVerifier nonceVerifier;

    private final SignatureProvider signatureProvider;

    @Autowired
    public SecuredResource(NonceVerifier nonceVerifier,
                           SignatureProvider signatureProvider) {
        this.nonceVerifier = nonceVerifier;
        this.signatureProvider = signatureProvider;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<CreateResponseDto> createDeposit(
            @RequestHeader(value = "key") String clientKey,
            @RequestBody @Valid CreateRequestDto createRequestDto) {
        log.info("Create requested with reference={}", createRequestDto.getReference());
        nonceVerifier.verifyOrThrow(clientKey, createRequestDto.getNonce());

        CreateResponseDto createResponseDto = null; // Call the service, create resource and return response dto

        return toResponseEntity(status(ACCEPTED), clientKey, createResponseDto);
    }

    @PostMapping("/find")
    ResponseEntity<FindResponseDto> findDepositFilteredV2(
            @RequestHeader(value = "key") String clientKey,
            @RequestBody @Valid FindFiltersDto findFiltersDto) {

        // No need to validate the noce for a GET api
        Page<ResourceDto> results = null; // call to the service to find using filters

        return toResponseEntity(status(HttpStatus.OK), clientKey, new FindResponseDto(findFiltersDto, results));
    }

    private <T> ResponseEntity<T> toResponseEntity(ResponseEntity.BodyBuilder bodyBuilder, String clientKey, T body) {
        val headers = SignatureHeaderProvider.headersWithSignature(signatureProvider.generateSignatureFromObject(clientKey, body));
        return bodyBuilder.headers(headers).body(body);
    }
}
