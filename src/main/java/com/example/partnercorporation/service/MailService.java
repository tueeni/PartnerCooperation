package com.example.partnercorporation.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public List<String> getEmailsWithAccessKeyword() throws MessagingException {
        List<String> emails = new ArrayList<>();

        Properties properties = new Properties();
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(properties);
        Store store = session.getStore("imap");
        store.connect("imap.mail.ru", "uxiir@mail.ru", "nHsRWWHiYsfJN8i9CwAM");

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        SearchTerm subjectTerm = new SubjectTerm("cooperation"); // Check for 'cooperation' in subject
        SearchTerm bodyTerm = new OrTerm(
                new BodyTerm("acceptance"), // Check for 'acceptance' in body
                new BodyTerm("access")
        );

        SearchTerm searchTerm = new AndTerm(subjectTerm, bodyTerm);

        Message[] messages = inbox.search(searchTerm);

        for (Message message : messages) {
            if (message instanceof MimeMessage) {
                MimeMessage mimeMessage = (MimeMessage) message;
                emails.add(mimeMessage.getSubject());
            }
        }

        inbox.close(false);
        store.close();

        return emails;
    }
    public List<String> getEmailsWithDeclineKeyword() throws MessagingException {
        List<String> emails = new ArrayList<>();

        Properties properties = new Properties();
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(properties);
        Store store = session.getStore("imap");
        store.connect("imap.mail.ru", "uxiir@mail.ru", "nHsRWWHiYsfJN8i9CwAM");

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        SearchTerm subjectTerm = new SubjectTerm("cooperation");
        SearchTerm bodyTerm = new OrTerm(
                new BodyTerm("decline"),
                new BodyTerm("declination")
        );

        SearchTerm searchTerm = new AndTerm(subjectTerm, bodyTerm);

        Message[] messages = inbox.search(searchTerm);

        for (Message message : messages) {
            if (message instanceof MimeMessage) {
                MimeMessage mimeMessage = (MimeMessage) message;
                emails.add(mimeMessage.getSubject());
            }
        }

        inbox.close(false);
        store.close();

        return emails;
    }

}