package com.coviam.crm.marketagent.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@ToString
@Document(collection = "Lead")
public class Lead {
    @Id
    String leadId;
    String leadName;
    String leadEmail;
    String category;
    String source;
    String status;
    String createdTime;
    String updateTime;
    String mailCount;
    List<String> comments;
    List<String> images;
    List<String> videos;
    List<String> documents;
}
