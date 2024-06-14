package com.example.partnercorporation.delegates;

import com.example.partnercorporation.service.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) {
        String fileName = (String) execution.getVariable("fileName");
        byte[] fileBytes = (byte[]) execution.getVariable("fileBytes");

        if (fileBytes != null && fileName != null && !fileName.isEmpty()) {
            emailService.sendEmail(fileName, fileBytes);
        } else {
            throw new IllegalArgumentException("The fileName or fileBytes variable is null or empty.");
        }
    }
}

