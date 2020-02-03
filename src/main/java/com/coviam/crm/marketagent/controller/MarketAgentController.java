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

    @Autowired
    LoginClient loginClient;

    @GetMapping("/get/{id}")
            void get(@PathVariable("id") String userId){
        System.out.println(loginClient.getUserById(userId));
    }

//    @PostMapping("/addLead")
//    ResponseEntity<Lead> addLead(@RequestBody AdDTO adDTO1){
//
//        String adDTO = "{\"adId\":\"5e329274a11c9a31cdbaa9a8\",\"tag\":\"Watches\",\"advertiserId\":\"1\",\"categoryName\":\"Lifestyle\",\"userId\":\"e11cc62e-e11b-46ce-b49f-2d35466ca00a \",\"description\":\"Fossil Analog Blue Dial Men's Watch-BQ2311\",\"source\":\"Quora\",\"targetUrl\":\"https://www.amazon.in/Fossil-Analog-Blue-Dial-Watch-BQ2311/dp/B07DMQ2SWB/ref=sxin_4?ascsubtag=amzn1.osa.7de818d2-4760-48da-aa0b-467ecebe4dcb.A21TJRUUN4KGV.en_IN&creativeASIN=B07DMQ2SWB&crid=1NCSXXNJ7BLPE&cv_ct_cx=watches+for+mens+stylish&cv_ct_id=amzn1.osa.7de818d2-4760-48da-aa0b-467ecebe4dcb.A21TJRUUN4KGV.en_IN&cv_ct_pg=search&cv_ct_wn=osp-single-source&keywords=watches+for+mens+stylish&linkCode=oas&pd_rd_i=B07DMQ2SWB&pd_rd_r=4ad2a8ab-d52b-41af-8cc2-305c321cf338&pd_rd_w=PM2h4&pd_rd_wg=vnTV4&pf_rd_p=8abf6964-7313-4310-9a0f-4858d74fcf4a&pf_rd_r=9A2KJQJRVGN5WCKGFJG3&qid=1580372540&s=watches&smid=A2XS4ABRR727IE&sprefix=watc%2Cshoes%2C289&sr=1-2-32235bf8-c8dc-423d-b49a-58af94d8b862&tag=htsyndicate-21\"}";
//        return new ResponseEntity<Lead>(marketAgentService.addNewLead(adDTO),HttpStatus.OK);
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
    @GetMapping("/getLeadListByMarketAgentId")
    ResponseEntity<List<Lead>> getLeadListByMarketAgentId(@RequestHeader("userId") String userId){
        System.out.println("==========++>ID:"+userId);
        return new ResponseEntity<>(marketAgentService.getLeadListByMarketAgentId(userId), HttpStatus.OK);
    }

    @GetMapping("/getMyListByMarketAgentId")
    ResponseEntity<List<Lead>> getMyListByMarketAgentId(@RequestHeader("userId") String userId){
        System.out.println("==========+-----+>ID:"+userId);
        return new ResponseEntity<>(marketAgentService.getMyListByMarketAgentId(userId), HttpStatus.OK);
    }

    @GetMapping("/getLeadByLeadId/{id}")
    ResponseEntity<Lead> getLeadByLeadId(@PathVariable("id") String leadId){
        return new ResponseEntity<Lead>(marketAgentService.getLeadByLeadId(leadId),HttpStatus.OK);
    }

    @PostMapping("/uploadComments")
    ResponseEntity<Lead> uploadComments(@RequestBody CommentsDTO commentsDTO){
        return new ResponseEntity<>(marketAgentService.upload(commentsDTO), HttpStatus.OK);
    }

    //request header
    @GetMapping("/getPendingLeadsById")
    ResponseEntity<Long> getPendingLeadsById(@RequestHeader("userId") String id){
        return new ResponseEntity<Long>(marketAgentService.getPendingLeadsById(id),HttpStatus.OK);
    }

    //request header
    @GetMapping("/getConvertedLeadsById")
    ResponseEntity<Long> getConvertedLeadsById(@RequestHeader("userId") String id){
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

    @GetMapping("/getClosedLeads")
    ResponseEntity<List<Lead>> getMyLeads(@RequestHeader("userId") String userId){
        return new ResponseEntity<List<Lead>>(marketAgentService.getMyLeads(userId),HttpStatus.OK);
    }

}
