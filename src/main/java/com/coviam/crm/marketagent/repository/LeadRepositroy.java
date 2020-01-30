package com.coviam.crm.marketagent.repository;

import com.coviam.crm.marketagent.document.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepositroy extends MongoRepository<Lead,String> {
}
