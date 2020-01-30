package com.coviam.crm.marketagent.repository;

import com.coviam.crm.marketagent.document.MarketAgent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketAgentRepository extends MongoRepository<MarketAgent,String> {
}
