package com.coviam.crm.marketagent.controller;

import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.document.MarketAgent;
import com.coviam.crm.marketagent.document.MarketAgentLead;
import com.coviam.crm.marketagent.dto.AssignDTO;
import com.coviam.crm.marketagent.dto.CommentsDTO;
import com.coviam.crm.marketagent.dto.MarketAgentDTO;
import com.coviam.crm.marketagent.service.MarketAgentService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public class MarketAgentController {

    @Autowired
    MarketAgentService marketAgentService;



    @PostMapping("/addMarketAgent")
    ResponseEntity<String> addMarketAgent(@RequestBody MarketAgentDTO marketAgentDTO){
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
        BeanUtils.copyProperties(marketAgentLead,assignDTO);
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

    @GetMapping("/getLeadListByMarketAgentId/{id}")
    ResponseEntity<List<Lead>> getLeadListByMarketAgentId(@PathVariable String marketingAgentid){
        return new ResponseEntity<>(marketAgentService.getLeadListByMarketAgentId(marketingAgentid), HttpStatus.OK);
    }

    @PostMapping("/uploadComments")
    ResponseEntity<Lead> uploadComments(@RequestBody CommentsDTO commentsDTO){
        return new ResponseEntity<>(marketAgentService.upload(commentsDTO), HttpStatus.OK);
    }


    @GetMapping("/getPendingLeadsById/{id}")
    ResponseEntity<Long> getPendingLeadsById(@PathVariable("id") String id){
        return new ResponseEntity<Long>(marketAgentService.getPendingLeadsById(id),HttpStatus.OK);
    }

    @GetMapping("/getConvertedLeadsById/{id}")
    ResponseEntity<Long> getConvertedLeadsById(@PathVariable("id") String id){
        return new ResponseEntity<Long>(marketAgentService.getConvertedLeadsById(id),HttpStatus.OK);
    }

}
