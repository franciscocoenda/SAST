package es.technest.security.api.security.nonce;

import es.technest.security.api.client.model.ClientId;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static es.technest.security.api.security.nonce.NonceRepository.NONCE_COLLECTION_NAME;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Repository
public class NonceRepositoryExtendedImpl implements NonceRepositoryExtended {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NonceRepositoryExtendedImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean update(ClientId clientId, Long nonce, Long window) {

        val updateNonceAndAddToUsedNonces = new Update();
        updateNonceAndAddToUsedNonces.inc("version", 1L);
        updateNonceAndAddToUsedNonces.max("maxNonce", nonce);

        val criteria = new Criteria()
                .and("clientId").is(clientId)
                .and("maxNonce").lt(window + nonce);

        val nonceQuery = new Query().addCriteria(criteria);

        if (window > 0) {
            nonceQuery.addCriteria(criteria.and("usedNonces").nin(nonce));
            updateNonceAndAddToUsedNonces.push("usedNonces")
                    .sort(DESC)
                    .slice(window.intValue())
                    .each(nonce);
        }

        val updateResult = mongoTemplate.updateFirst(nonceQuery, updateNonceAndAddToUsedNonces, NONCE_COLLECTION_NAME);

        return updateResult.getMatchedCount() == 1;
    }
}