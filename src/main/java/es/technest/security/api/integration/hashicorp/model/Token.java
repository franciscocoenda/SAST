package es.technest.security.api.integration.hashicorp.model;

import lombok.Value;

import java.io.Serializable;

@Value
public class Token implements Serializable {
    private String value;
}
