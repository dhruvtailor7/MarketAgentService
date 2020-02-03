package com.coviam.crm.marketagent.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LeadDTO {
    private String leadId;
    private String leadName;
    private String leadEmail;
    private String category;
    private String source;
    private String status;
    private String createdTime;
    private String updateTime;
    private String mailCount;
    private List<String> comments;
    private List<String> images;
    private List<String> videos;
    private List<String> documents;

}
