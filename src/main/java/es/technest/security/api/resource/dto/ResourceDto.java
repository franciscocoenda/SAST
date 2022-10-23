package es.technest.security.api.resource.dto;

import lombok.Value;

import java.net.URL;
import java.time.Instant;

@Value
public class ResourceDto {

    URL callbackUrl;

    String reference;

    String customerId;

    Instant createdDate;

    Instant expiryDate;

}
