package com.coviam.crm.marketagent.service;

import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.document.MarketAgent;
import com.coviam.crm.marketagent.document.MarketAgentLead;
import com.coviam.crm.marketagent.dto.CommentsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface MarketAgentService {
    MarketAgent addMmarketAgent(MarketAgent marketAgent);

    List<Lead> getAllLeads();

    List<Lead> getLeadsByCategory(String category);


    void deleteMarketAgent(String mId);

    void addMarketAgentLead(MarketAgentLead marketAgentLead);

    List<MarketAgent> getMarketAgentByCategory(String category);

    void closeLead(String leadId);

    Lead upload(CommentsDTO commentsDTO);

    List<Lead> getLeadListByMarketAgentId(String marketingAgentid);

    Long getPendingLeadsById(String id);

    Long getConvertedLeadsById(String id);

    List<String> getCategoryList();

    void addLead(Lead lead);

    void mail() throws JsonProcessingException;

    List<MarketAgent> getMAList();

    Lead getLeadByLeadId(String leadId);

    List<Lead> getMyLeads(String marketingAgentId);

    List<Lead> getMyListByMarketAgentId(String userId);

//    Lead addNewLead(String adDTO);
}
