package com.coviam.crm.marketagent.mailtemplete;

import com.coviam.crm.marketagent.document.Lead;
import com.coviam.crm.marketagent.dto.MailDTO;

public class MailTemplateAdmin {
    public static MailDTO mail(Lead lead){
        MailDTO mailDTO = new MailDTO();
        mailDTO.setUserEmail("jainilpatel807@gmail.com");
        long mailNumber = Long.parseLong(lead.getMailCount());
        switch ((int) mailNumber){
            case 0:
            case 1:
                mailDTO.setContent("Hey Admin,\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been generated since "+lead.getUpdateTime()+" minutes. Please assign it to an agent.\n" +
                        "\n" +
                        "Thank you.");
                break;
            case 2:
            case 3:
                mailDTO.setContent("Hey Admin,\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been generated since "+lead.getUpdateTime()+" minutes. Please assign it to an agent as soon as possible.\n" +
                        "\n" +
                        "Thank you.");
                break;
            case 4:
                mailDTO.setContent("Admin,\n" +
                        "\n" +
                        "The lead, "+lead.getLeadId()+", has been generated since "+lead.getUpdateTime()+" minutes. Assign it to an agent now!!!");
                break;
        }
        return mailDTO;
    }
}
