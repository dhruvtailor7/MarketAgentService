package com.coviam.crm.marketagent.client;

import com.coviam.crm.marketagent.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "login",url = "http://172.16.20.121:8080/controller")
public interface LoginClient {

    @GetMapping("/getUserDetails/{id}")
    UserDTO getUserById(@PathVariable("id") String userId);

}
