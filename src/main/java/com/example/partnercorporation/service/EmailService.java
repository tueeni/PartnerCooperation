package com.example.partnercorporation.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String fileName, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("pdf.holder@mail.ru");
            helper.setSubject("Generated PDF");
            helper.setText("Request for collaboration with the company " + fileName + ". Could you please indicate" +
                    " your response, choosing between acceptance or declination?");
            helper.setFrom("uxiir@mail.ru");

            ByteArrayResource byteArrayResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, byteArrayResource);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
