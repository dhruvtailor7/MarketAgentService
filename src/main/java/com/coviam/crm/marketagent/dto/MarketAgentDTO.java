package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketAgentDTO {
    String marketingAgentId;
    String marketingAgentName;
    String marketingAgentEmail;
    long leadsResolved;
    long leadPending;
    String category;
}
