package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsDTO {
    private String leadId;
    private String comment;
    private String image;
    private String video;
    private String document;
}
