package com.coviam.crm.marketagent.service.impl;

import com.coviam.crm.marketagent.client.LoginClient;
import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.document.MarketAgent;
import com.coviam.crm.marketagent.document.MarketAgentLead;
import com.coviam.crm.marketagent.dto.AdDTO;
import com.coviam.crm.marketagent.dto.CommentsDTO;
import com.coviam.crm.marketagent.dto.MailDTO;
import com.coviam.crm.marketagent.dto.UserDTO;
import com.coviam.crm.marketagent.repository.LeadRepositroy;
import com.coviam.crm.marketagent.repository.MarketAgentLeadRepository;
import com.coviam.crm.marketagent.repository.MarketAgentRepository;
import com.coviam.crm.marketagent.service.MarketAgentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.coviam.crm.marketagent.mailtemplete.MailTemplateAdmin;
import com.coviam.crm.marketagent.mailtemplete.MailTempleteAgent;
import sun.jvm.hotspot.oops.Mark;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Component
public class MarketAgentServiceImpl implements MarketAgentService {

    @Autowired
    LoginClient loginClient;

    @Autowired
    LeadRepositroy leadRepositroy;
    @Autowired
    MarketAgentRepository marketAgentRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    MarketAgentLeadRepository marketAgentLeadRepository;

    private static final String[] category = {"","Literature","Technology","Lifestyle","Movies","Food","Sports"};
    private String secret="testProject";
    public Claims parseToken(String token) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token).getBody();
        return claims;
    }
    @Override
    public MarketAgent addMmarketAgent(MarketAgent marketAgent) {
        System.out.println("creating  "+marketAgent);
        return marketAgentRepository.save(marketAgent);
    }


    @Override
    public List<Lead> getMyLeads(String marketingAgentid) {
        List<String> leadIds = marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> marketAgentLead.getMarketingAgentId().equals(marketingAgentid))
                .map(marketAgentLead -> marketAgentLead.getLeadId()).collect(Collectors.toList());

        List<Lead> leadList = (List<Lead>) leadRepositroy.findAllById(leadIds);
        return (List<Lead>) leadList.stream().filter(lead -> lead.getStatus().equals("closed")).collect(Collectors.toList());
    }

    @KafkaListener(topics = "clicks" , groupId = "group-id",containerFactory = "kafkaListenerContainerFactory")
//    @Override
    public Lead addNewLead(String adDTO) {
        Lead newLead = new Lead();
        AdDTO adDTO1=new AdDTO();
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            adDTO1 = objectMapper.readValue(adDTO,AdDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //newLead.setLeadId(adDTO1.getAccessToken());

       // String token=adDTO1.getUserId().substring(7);
        //Claims claims=this.parseToken(token);
        System.out.println(adDTO1);
        String userId = adDTO1.getUserId();
        BeanUtils.copyProperties(adDTO1,newLead);
        UserDTO userDTO = loginClient.getUserById(userId.trim());
        newLead.setStatus("open");
        newLead.setLeadId(userId);
        newLead.setLeadName(userDTO.getName());
        newLead.setLeadEmail(userDTO.getEmailAddress());
        newLead.setMailCount("0");
        newLead.setCategory(adDTO1.getCategoryName());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm:ss");

        LocalDateTime date=LocalDateTime.now();
        System.out.println(df.format(date));
        newLead.setCreatedTime(df.format(date));
        newLead.setUpdateTime(df.format(date));
        return leadRepositroy.save(newLead);
        }

    @Override
    public Lead upload(CommentsDTO commentsDTO) {
        Lead lead = leadRepositroy.findById(commentsDTO.getLeadId()).get();
        lead.getComments().add(commentsDTO.getComment());
        lead.getImages().add(commentsDTO.getImage());
        lead.getVideos().add(commentsDTO.getVideo());
        lead.getDocuments().add(commentsDTO.getDocument());

        return leadRepositroy.save(lead);
    }

    @Override
    public List<Lead> getLeadListByMarketAgentId(String marketingAgentid) {
        List<String> leadIds = marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> marketAgentLead.getMarketingAgentId().equals(marketingAgentid))
                .map(marketAgentLead -> marketAgentLead.getLeadId()).collect(Collectors.toList());

        List<Lead> leadList = (List<Lead>) leadRepositroy.findAllById(leadIds);
        System.out.println(leadList);
        return (List<Lead>) leadList.stream().filter(lead -> lead.getStatus().equals("open")).collect(Collectors.toList());
    }

    @Override
    public void deleteMarketAgent(String mId) {
        marketAgentRepository.deleteById(mId);
    }

    @Override
    public List<Lead> getLeadsByCategory(String category) {
        List<Lead> leadList = (ArrayList<Lead>)leadRepositroy.findAll();
        return leadList.stream().filter(lead -> lead.getCategory().contains(category)).collect(Collectors.toList());

    }



    @Override
    public List<Lead> getAllLeads() {
        return (List<Lead>) leadRepositroy.findAll();
    }

    @Override
    public void addMarketAgentLead(MarketAgentLead marketAgentLead) {
        System.out.println(marketAgentLead);
        MarketAgent marketAgent=marketAgentRepository.findById(marketAgentLead.getMarketingAgentId()).get();
        Lead lead=leadRepositroy.findById(marketAgentLead.getLeadId()).get();
        marketAgent.setLeadPending(marketAgent.getLeadPending()+1);
        lead.setStatus("in progress");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime date=LocalDateTime.now();
        lead.setUpdateTime(df.format(date));
        lead.setMailCount("0");
        leadRepositroy.save(lead);
        marketAgentRepository.save(marketAgent);
        marketAgentLeadRepository.save(marketAgentLead);

    }

    @Override
    public List<Lead> getMyListByMarketAgentId(String marketingAgentid) {
        List<String> leadIds = marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> marketAgentLead.getMarketingAgentId().equals(marketingAgentid))
                .map(marketAgentLead -> marketAgentLead.getLeadId()).collect(Collectors.toList());

        List<Lead> leadList = (List<Lead>) leadRepositroy.findAllById(leadIds);
        System.out.println(leadList);
        return (List<Lead>) leadList.stream().filter(lead -> lead.getStatus().equals("in progress")).collect(Collectors.toList());
    }

    @Override
    public List<MarketAgent> getMarketAgentByCategory(String category) {
        ArrayList<MarketAgent> marketAgentArrayList=(ArrayList<MarketAgent>) marketAgentRepository.findAll();
        return marketAgentArrayList.stream().filter(marketAgent -> marketAgent.getCategory().contains(category)).collect(Collectors.toList());

    }

    @Override
    public void closeLead(String leadId) {

        MarketAgentLead marketAgentLeadAssigned=marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> leadId.equals(marketAgentLead.getLeadId())).collect(Collectors.toList()).get(0);
        MarketAgent marketAgent=marketAgentRepository.findById(marketAgentLeadAssigned.getMarketingAgentId()).get();
        marketAgent.setLeadPending(marketAgent.getLeadPending()-1);
        marketAgent.setLeadsConverted(marketAgent.getLeadsConverted()+1);
        marketAgentRepository.save(marketAgent);
        Lead lead=leadRepositroy.findById(leadId).get();
        lead.setStatus("closed");
        leadRepositroy.save(lead);

    }

    @Override
    public List<String> getCategoryList() {
        List<String> list=new ArrayList<>();
        for (int i=1;i<category.length;i++){
            list.add(category[i]);
        }
        return list;
    }

    @Override
    public Lead getLeadByLeadId(String leadId) {
        return leadRepositroy.findById(leadId).get();
    }

    @Override
    public void addLead(Lead lead) {
        leadRepositroy.save(lead);
    }

    @Override
    public Long getPendingLeadsById(String id) {

        return marketAgentRepository.findById(id).get().getLeadPending();
    }

    @Override
    public Long getConvertedLeadsById(String id) {
        return marketAgentRepository.findById(id).get().getLeadsConverted();
    }

    @Override
    public void mail() throws JsonProcessingException {
        MailDTO mailDTO=new MailDTO();
        mailDTO.setUserEmail("dhruvtailor7@gmail.com");
        mailDTO.setContent("asdfghjkwertyuio");
        ObjectMapper objectMapper=new ObjectMapper();
        kafkaTemplate.send("mail",objectMapper.writeValueAsString(mailDTO));
    }

    @Override
    public List<MarketAgent> getMAList() {
        return marketAgentRepository.findAll();
    }

    @Scheduled(fixedDelay = 600000)
    private void sendMails() {
        System.out.println("hi");
        List<Lead> leadList = leadRepositroy.findAll();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        leadList.stream().forEach(lead -> {
            if ((lead.getStatus()).equals("open")) {
                LocalDateTime now = LocalDateTime.now();

                if (dtf.format(now.minusMinutes(15)).compareTo(lead.getUpdateTime()) > 0) {

                    MailDTO mailDTO = MailTemplateAdmin.mail(lead);

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        if(mailDTO.getUserEmail()!=null)
                        kafkaTemplate.send("mail", objectMapper.writeValueAsString(mailDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (Long.parseLong(lead.getMailCount()) <= 4) {
                        LocalDateTime now1 = LocalDateTime.now();
                        lead.setUpdateTime(dtf.format(now1));
                        lead.setMailCount(String.valueOf(Integer.parseInt(lead.getMailCount()) + 1));
                    }

                    else {
                        lead.setMailCount("0");
                        lead.setStatus("discarded");
                    }

                    leadRepositroy.save(lead);


                }

            } else if ((lead.getStatus()).equals("in progress")) {
                LocalDateTime now = LocalDateTime.now();

                if (dtf.format(now.minusMinutes(15)).compareTo(lead.getUpdateTime()) > 0) {

                    AtomicReference<String> marketAgentid = new AtomicReference<>("");
                    marketAgentLeadRepository.findAll().stream().forEach(marketAgentLead -> {
                        System.out.println(marketAgentLead.getLeadId());
                        System.out.println(lead.getLeadId());
                        if ((marketAgentLead.getLeadId()).equals(lead.getLeadId())) {
                            marketAgentid.set(marketAgentLead.getMarketingAgentId());
                            return;
                        }
                    });
                    System.out.println("======="+marketAgentid);
                    MailDTO mailDTO = MailTempleteAgent.mail(lead, marketAgentRepository.findById(marketAgentid.get()).get());

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        if(mailDTO.getUserEmail()!=null)
                        kafkaTemplate.send("mail", objectMapper.writeValueAsString(mailDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (Long.parseLong(lead.getMailCount()) <= 4) {
                        LocalDateTime now1 = LocalDateTime.now();
                        lead.setUpdateTime(dtf.format(now1));
                        lead.setMailCount(String.valueOf(Integer.parseInt(lead.getMailCount()) + 1));
                    }
                    else {
                        MarketAgent marketAgent=marketAgentRepository.findById(marketAgentid.get()).get();
                        marketAgent.setLeadPending(marketAgent.getLeadPending()-1);

                        MarketAgentLead marketAgentLead = marketAgentLeadRepository.findAll().stream().filter(
                             marketAgentLead1 -> marketAgentLead1.getMarketingAgentId().equals(marketAgentid.get()) && marketAgentLead1.getLeadId().equals(lead.getLeadId())
                        ).collect(Collectors.toList()).get(0);
                        marketAgentLeadRepository.delete(marketAgentLead);

                        LocalDateTime now1 = LocalDateTime.now();
                        lead.setUpdateTime(dtf.format(now1));
                        lead.setMailCount("0");
                        lead.setStatus("open");
                    }

                    leadRepositroy.save(lead);

                }
            }
        });


    }
}
