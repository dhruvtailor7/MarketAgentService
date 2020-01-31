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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.coviam.crm.marketagent.mailtemplete.MailTemplateAdmin;
import com.coviam.crm.marketagent.mailtemplete.MailTempleteAgent;
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

    @Override
    public void addMmarketAgent(MarketAgent marketAgent) {
        marketAgentRepository.save(marketAgent);
    }

    @KafkaListener(topics = "clicks" , groupId = "group-id")
    void addNewLead(AdDTO adDTO){
        Lead newLead = new Lead();
        BeanUtils.copyProperties(adDTO,newLead);
        UserDTO userDTO = loginClient.getUserById(adDTO.getUserId());
        newLead.setStatus("open");
        newLead.setLeadName(userDTO.getName());
        newLead.setLeadEmail(userDTO.getEmail());
        newLead.setMailCount("0");
        newLead.setCategory(category[Integer.parseInt(adDTO.getCategoryId())]);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm:ss");

        LocalDateTime date=LocalDateTime.now();
        System.out.println(df.format(date));
        newLead.setCreatedTime(df.format(date));
        newLead.setUpdateTime(df.format(date));
        leadRepositroy.save(newLead);

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
        List<String> leadIds = marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> marketAgentLead.getMarketAgentId().equals(marketingAgentid))
                .map(marketAgentLead -> marketAgentLead.getLeadId()).collect(Collectors.toList());

        return (List<Lead>) leadRepositroy.findAllById(leadIds);
    }

    @Override
    public void deleteMarketAgent(String mId) {
        marketAgentRepository.deleteById(mId);
    }

    @Override
    public List<Lead> getLeadsByCategory(String category) {
        List<Lead> leadList = (ArrayList<Lead>)leadRepositroy.findAll();
        return leadList.stream().filter(lead -> lead.getCategory().equals(category)).collect(Collectors.toList());

    }



    @Override
    public List<Lead> getAllLeads() {
        return (List<Lead>) leadRepositroy.findAll();
    }

    @Override
    public void addMarketAgentLead(MarketAgentLead marketAgentLead) {
        MarketAgent marketAgent=marketAgentRepository.findById(marketAgentLead.getMarketAgentId()).get();
        Lead lead=leadRepositroy.findById(marketAgentLead.getLeadId()).get();
        marketAgent.setLeadPending(marketAgent.getLeadPending()+1);
        lead.setStatus("in progress");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime date=LocalDateTime.now();
        lead.setUpdateTime(df.format(date));
        leadRepositroy.save(lead);
        marketAgentRepository.save(marketAgent);
        marketAgentLeadRepository.save(marketAgentLead);

    }

    @Override
    public List<MarketAgent> getMarketAgentByCategory(String category) {
        ArrayList<MarketAgent> marketAgentArrayList=(ArrayList<MarketAgent>) marketAgentRepository.findAll();
        return marketAgentArrayList.stream().filter(marketAgent -> marketAgent.getCategory().equals(category)).collect(Collectors.toList());

    }

    @Override
    public void closeLead(String leadId) {

        MarketAgentLead marketAgentLeadAssigned=marketAgentLeadRepository.findAll().stream().filter(marketAgentLead -> leadId.equals(marketAgentLead.getLeadId())).collect(Collectors.toList()).get(0);
        marketAgentLeadRepository.delete(marketAgentLeadAssigned);
        MarketAgent marketAgent=marketAgentRepository.findById(marketAgentLeadAssigned.getMarketAgentId()).get();
        marketAgent.setLeadPending(marketAgent.getLeadPending()-1);
        marketAgent.setLeadsConverted(marketAgent.getLeadsConverted()+1);
        marketAgentRepository.save(marketAgent);
        Lead lead=leadRepositroy.findById(leadId).get();
        lead.setStatus("closed");
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

    @Scheduled(fixedDelay = 600000)
    private void sendMails() {
        List<Lead> leadList = leadRepositroy.findAll();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        leadList.stream().forEach(lead -> {
            if ((lead.getStatus()).equals("open")) {
                LocalDateTime now = LocalDateTime.now();

                if (dtf.format(now.minusMinutes(15)).compareTo(lead.getUpdateTime()) > 0) {

                    MailDTO mailDTO = MailTemplateAdmin.mail(lead);

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        kafkaTemplate.send("Mail", objectMapper.writeValueAsString(mailDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (Long.parseLong(lead.getMailCount()) < 4)
                        lead.setMailCount(lead.getMailCount() + 1);

                    else
                        lead.setStatus("discarded");


                }

            } else if ((lead.getStatus()).equals("in progress")) {
                LocalDateTime now = LocalDateTime.now();

                if (dtf.format(now.minusMinutes(15)).compareTo(lead.getUpdateTime()) > 0) {

                    AtomicReference<String> marketAgentid = new AtomicReference<>("");
                    marketAgentLeadRepository.findAll().stream().forEach(marketAgentLead -> {
                        if ((marketAgentLead.getLeadId()).equals(lead.getLeadId())) {
                            marketAgentid.set(marketAgentLead.getMarketAgentId());
                            return;
                        }
                    });

                    MailDTO mailDTO = MailTempleteAgent.mail(lead, marketAgentRepository.findById(marketAgentid.get()).get());

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        kafkaTemplate.send("Mail", objectMapper.writeValueAsString(mailDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (Long.parseLong(lead.getMailCount()) < 4)
                        lead.setMailCount(lead.getMailCount() + 1);

                    else
                        lead.setStatus("open");

                }
            }
        });
    }
}
