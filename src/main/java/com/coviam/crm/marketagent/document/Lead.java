package com.coviam.crm.marketagent.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@Document(collection = "Lead")
public class Lead {
    @Id
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

    public Lead() {
        comments = new ArrayList<>();
        images = new ArrayList<>();
        videos = new ArrayList<>();
        documents = new ArrayList<>();
    }
}
