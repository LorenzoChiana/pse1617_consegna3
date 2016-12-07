package com.example.brenno.pse1617_consegna3;

/**
 * Created by brenno on 07/12/2016.
 */

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailClient {
    private static final String emailPort = "587";
    private static final String smtpAuth = "true";
    private static final String starttls = "true";
    private static final String emailHost = "smtp.gmail.com";
    private String fromEmail;
    private String fromPassword;
    private Properties emailProperties;
    private Session mailSession;
    public GmailClient(String fromEmail, String fromPassword) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
    }
    public void sendEmail(GmailEmail email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailMessage = createEmailMessage(email.getSubject(), email.getBody(), email.getRecipients());
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GmailClient", "Email sent successfully.");
    }
    private MimeMessage createEmailMessage(String emailSubject, String emailBody, List<String> toEmailList) throws MessagingException, UnsupportedEncodingException {
        mailSession = Session.getDefaultInstance(emailProperties, null);
        MimeMessage emailMessage = new MimeMessage(mailSession);
        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {
            Log.i("GmailClient","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        }
        emailMessage.setSubject(emailSubject);
        //emailMessage.setContent(emailBody, "text/html");// for a html email
        emailMessage.setText(emailBody);// for a text email
        Log.i("GmailClient", "Email Message created.");
        return emailMessage;
    }
}
