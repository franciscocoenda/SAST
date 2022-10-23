package es.technest.security.api.security.signature.model;

import es.technest.security.api.client.model.ClientId;
import es.technest.security.api.integration.hashicorp.model.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "signature-secret")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignatureSecretToken {

    @Id
    private ClientId clientId;

    @NotNull
    private Token token;

}
