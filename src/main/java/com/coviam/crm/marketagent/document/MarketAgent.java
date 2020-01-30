package com.coviam.crm.marketagent.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@Document(collection = "MarketAgent")
public class MarketAgent {
    @Id
    String marketingAgentId;
    String marketingAgentName;
    String marketingAgentEmail;
    long leadsConverted;
    long leadPending;
    String category;
}
