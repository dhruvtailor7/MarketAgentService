package com.coviam.crm.marketagent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private String userId;
    private String name;
    private String emailAddress;
    private String password;
}
