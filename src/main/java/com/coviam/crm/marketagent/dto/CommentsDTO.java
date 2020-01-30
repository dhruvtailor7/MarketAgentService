package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsDTO {
    String leadId;
    String comment;
    String image;
    String video;
    String document;
}
