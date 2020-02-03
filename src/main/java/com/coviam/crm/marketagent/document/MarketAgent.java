package com.coviam.crm.marketagent.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@ToString
@Document(collection = "MarketAgent")
public class MarketAgent {
    @Id
    private String marketingAgentId;
    private String marketingAgentName;
    private String marketingAgentEmail;
    private long leadsConverted;
    private long leadPending;
    private List<String> category;
}
