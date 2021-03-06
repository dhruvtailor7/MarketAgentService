package com.coviam.crm.marketagent.mailtemplete;

import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.document.MarketAgent;
import com.coviam.crm.marketagent.dto.MailDTO;

public class MailTempleteAgent {
    public static MailDTO mail(Lead lead, MarketAgent marketAgent){
        MailDTO mailDTO = new MailDTO();
        mailDTO.setUserEmail(marketAgent.getMarketingAgentEmail());
        long mailNumber = Long.parseLong(lead.getMailCount());
        switch ((int) mailNumber){
            case 0:
            case 1:
                mailDTO.setContent("Hey "+marketAgent.getMarketingAgentName()+",\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been assigned to you since "+lead.getUpdateTime()+". Please resolve the matter.\n" +
                        "\n" +
                        "Thank you.");
                break;
            case 2:
            case 3:
                mailDTO.setContent("Hey "+marketAgent.getMarketingAgentName()+",\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been assigned to you since "+lead.getUpdateTime()+". Please resolve the matter as soon as possible.\n" +
                        "\n" +
                        "Thank you.");
                break;
            case 4:
                mailDTO.setContent(""+marketAgent.getMarketingAgentName()+"\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been assigned to you since "+lead.getUpdateTime()+" . Resolve the matter immediately ");
                break;
        }
        return mailDTO;
    }
}
