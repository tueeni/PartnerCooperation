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
    private JavaMailSender javaMailSender;

    public void sendEmail(String fileName, byte[] pdfBytes) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("pdf.holder@mail.ru");
            helper.setSubject("Application for cooperation " + removePdfExtension(fileName));
            helper.setText("Request for collaboration with the company " + removePdfExtension(fileName) + ". Could you please indicate" +
                    " your response");
            helper.setFrom("uxiir@mail.ru");

            ByteArrayResource byteArrayResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, byteArrayResource);

            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removePdfExtension(String fileName) {
        if (fileName.toLowerCase().endsWith(".pdf")) {
            return fileName.substring(0, fileName.length() - 4); // Remove last 4 characters (.pdf)
        }
        return fileName;
    }
}