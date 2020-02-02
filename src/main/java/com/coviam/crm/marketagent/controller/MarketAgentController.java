package com.coviam.crm.marketagent.controller;

import com.coviam.crm.marketagent.client.LoginClient;
import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.document.MarketAgent;
import com.coviam.crm.marketagent.document.MarketAgentLead;
import com.coviam.crm.marketagent.dto.*;
import com.coviam.crm.marketagent.service.MarketAgentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MarketAgentController {

    @Autowired
    MarketAgentService marketAgentService;


//    @GetMapping("/getData/{token}")
//    void get(@PathVariable("token") String token){
//
//        Claims claims=this.parseToken(token);
//        String userId = String.valueOf(claims.get("userId")).trim();
//        System.out.println(userId);
//        UserDTO userDTO = loginClient.getUserById("538842bf-4055-4add-9648-27ba89b8229c");
//        System.out.println(userDTO);
//    }

    @PostMapping("/addMarketAgent")
    ResponseEntity<String> addMarketAgent(@RequestBody MarketAgentDTO marketAgentDTO){
        System.out.println("got   "+marketAgentDTO);
        MarketAgent marketAgent=new MarketAgent();
        BeanUtils.copyProperties(marketAgentDTO,marketAgent);
        marketAgentService.addMmarketAgent(marketAgent);
        return new ResponseEntity<String>("Success",HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteMarketAgent/{mId}")
    public ResponseEntity<String> deleteSupportAgent(@PathVariable("mId") String mId){
        marketAgentService.deleteMarketAgent(mId);
        return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
    }

    @PostMapping("/assignMarketAgent")
    ResponseEntity<String> assign(@RequestBody AssignDTO assignDTO){
        MarketAgentLead marketAgentLead=new MarketAgentLead();
        BeanUtils.copyProperties(assignDTO,marketAgentLead);
        marketAgentService.addMarketAgentLead(marketAgentLead);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/getAllLeads")
    ResponseEntity<List<Lead>> getAllLeads(){
        return new ResponseEntity<>(marketAgentService.getAllLeads(),HttpStatus.OK);
    }

    @GetMapping("/getLeadsByCategory/{category}")
    ResponseEntity<List<Lead>> getLeadsByCategory(@PathVariable("category") String category){
        return new ResponseEntity<>(marketAgentService.getLeadsByCategory(category),HttpStatus.OK);
    }

    @GetMapping("/getMarketAgentByCategory/{category}")
    ResponseEntity<List<MarketAgent>> getMarketAgentByCategory(@PathVariable("category") String category){
        return new ResponseEntity<List<MarketAgent>>(marketAgentService.getMarketAgentByCategory(category),HttpStatus.OK);
    }

    @GetMapping("/closeLead/{leadId}")
    ResponseEntity<String> closeLead(@PathVariable("leadId") String leadId){
        marketAgentService.closeLead(leadId);
        return new ResponseEntity<String>("Success",HttpStatus.OK);
    }

    //request header
    @GetMapping("/getLeadListByMarketAgentId/{id}")
    ResponseEntity<List<Lead>> getLeadListByMarketAgentId(@PathVariable("id") String marketingAgentid){
        return new ResponseEntity<>(marketAgentService.getLeadListByMarketAgentId(marketingAgentid), HttpStatus.OK);
    }


    @PostMapping("/uploadComments")
    ResponseEntity<Lead> uploadComments(@RequestBody CommentsDTO commentsDTO){
        return new ResponseEntity<>(marketAgentService.upload(commentsDTO), HttpStatus.OK);
    }

    //request header
    @GetMapping("/getPendingLeadsById/{id}")
    ResponseEntity<Long> getPendingLeadsById(@PathVariable("id") String id){
        return new ResponseEntity<Long>(marketAgentService.getPendingLeadsById(id),HttpStatus.OK);
    }

    //request header
    @GetMapping("/getConvertedLeadsById/{id}")
    ResponseEntity<Long> getConvertedLeadsById(@PathVariable("id") String id){
        return new ResponseEntity<Long>(marketAgentService.getConvertedLeadsById(id),HttpStatus.OK);
    }

    @GetMapping("/getCategoryList")
    ResponseEntity<List<String>> getCategoryList(){
        return new ResponseEntity<List<String>>(marketAgentService.getCategoryList(),HttpStatus.OK);
    }

    @PostMapping("/addLead")
    ResponseEntity<String> addLead(@RequestBody LeadDTO leadDTO){
        Lead lead=new Lead();
        BeanUtils.copyProperties(leadDTO,lead);
        marketAgentService.addLead(lead);
        return new ResponseEntity<String>("Success",HttpStatus.OK);
    }

    @GetMapping("/sendMail")
    void mail() throws JsonProcessingException {
        marketAgentService.mail();
    }

    @GetMapping("/getAllMarketAgent")
    ResponseEntity<List<MarketAgent>> getMAList(){
        return new ResponseEntity<>(marketAgentService.getMAList(),HttpStatus.OK);
    }

}
