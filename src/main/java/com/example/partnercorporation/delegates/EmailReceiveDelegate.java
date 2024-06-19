package com.example.partnercorporation.delegates;

import com.example.partnercorporation.service.MailService;
import jakarta.mail.MessagingException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
@Component
public class EmailReceiveDelegate implements JavaDelegate {

    @Autowired
    private MailService mailService;

    @Override
    public void execute(DelegateExecution execution) throws MessagingException {
        String emailContent = String.valueOf(mailService.getEmailsWithAccessKeyword());
        execution.setVariable("emailContent", emailContent);
        System.out.println(emailContent);
    }
}
