package com.example.partnercorporation.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class LogDataDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String companyName = (String) execution.getVariable("companyName");
        String contactFace = (String) execution.getVariable("contactFace");
        String phone = (String) execution.getVariable("phone");
        String email = (String) execution.getVariable("email");

        System.out.println("Received company name: " + companyName);
        System.out.println("Received contact face: " + contactFace);
        System.out.println("Received phone number: " + phone);
        System.out.println("Received email: " + email);
    }
}
