package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MarketAgentDTO {
    private String marketingAgentId;
    private String marketingAgentName;
    private String marketingAgentEmail;
    private long leadsResolved;
    private long leadPending;
    private List<String> category;
}
