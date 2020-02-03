package com.coviam.crm.marketagent.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@Document(collection = "MarketAgentLead")
public class MarketAgentLead {
    @Id
    private String _id;
    private String marketingAgentId;
    private String leadId;
}
