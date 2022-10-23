package es.technest.security.api.resource.dto;

import es.technest.security.api.resource.dto.validators.UrlIsHttpHostConstrain;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URL;


@Value
public class CreateRequestDto {

    @NotNull
    Long nonce;

    @NotEmpty
    String reference;

    String customerId;

    @UrlIsHttpHostConstrain
    URL callbackUrl;
}
