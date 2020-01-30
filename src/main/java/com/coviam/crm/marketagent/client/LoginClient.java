package com.coviam.crm.marketagent.client;

import com.coviam.crm.marketagent.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "login")
public interface LoginClient {

    @GetMapping("/getUserById/{id}")
    UserDTO getUserById(@PathVariable("id") String userId);

}
