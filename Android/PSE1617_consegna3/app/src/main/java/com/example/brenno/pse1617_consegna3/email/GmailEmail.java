package com.example.brenno.pse1617_consegna3.email;

import java.util.List;

public class GmailEmail {
    private List<String> toEmailList;
    private String emailSubject;
    private String emailBody;
    public GmailEmail(String emailSubject, String emailBody, List<String> toEmailList){
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }
    public String getBody(){
        return this.emailBody;
    }
    public String getSubject(){
        return this.emailSubject;
    }
    public List<String> getRecipients(){
        return this.toEmailList;
    }
}