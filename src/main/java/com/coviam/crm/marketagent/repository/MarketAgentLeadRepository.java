package com.coviam.crm.marketagent.repository;

import com.coviam.crm.marketagent.document.MarketAgentLead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface MarketAgentLeadRepository extends MongoRepository<MarketAgentLead,String> {
}
