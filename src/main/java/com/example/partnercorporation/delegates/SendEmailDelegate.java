package com.example.partnercorporation.delegates;

import com.example.partnercorporation.service.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendEmailDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) {
        String companyName = (String) execution.getVariable("companyName");
        byte[] pdfBytes = (byte[]) execution.getVariable(companyName);

        if (pdfBytes != null) {
            emailService.sendEmail(companyName, pdfBytes);

        } else {
            throw new IllegalArgumentException("The generatedPdf variable is null.");
        }
    }
}
