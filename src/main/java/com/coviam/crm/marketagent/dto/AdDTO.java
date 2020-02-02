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
    private String categoryName;
    private String source;
    private String tag;
    private String advertiserId;
    private String description;
    private String targetUrl;
}
