package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdDTO {
    private String adId;
    private String userId;
    private String categoryId;
    private String source;
}
