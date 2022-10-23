package es.technest.security.api.integration.hashicorp;

import es.technest.security.api.integration.HealthAware;
import es.technest.security.api.integration.hashicorp.model.Token;

public interface HashiCorpClient extends HealthAware {
    <T> T retrieveData(Token token, Class<T> classType);
    <T> Token save(T data);
    <T> Token save(T data, Integer ttl);
}
