package com.example.partnercorporation.controller;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("/sendEmail")
    public String sendTestEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("pdf.holder@mail.ru");
            helper.setSubject("Test Email");
            helper.setText("This is a test email.Again попытка");
            helper.setFrom("uxiir@mail.ru");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }

        return "Email sent successfully!";
    }
}
