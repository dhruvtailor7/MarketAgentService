package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdDTO {
    String adId;
    String leadId;
    String category;
    String source;
}
