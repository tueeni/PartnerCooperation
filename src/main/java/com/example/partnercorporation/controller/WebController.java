package com.example.partnercorporation.controller;


import com.example.partnercorporation.entity.FormData;
import com.example.partnercorporation.repository.FormDataRepository;
import com.example.partnercorporation.service.EmailService;
import com.example.partnercorporation.service.MailService;
import com.example.partnercorporation.service.PdfGenerationService;
import jakarta.mail.MessagingException;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormDataRepository formDataRepository;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailService mailService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @PostMapping("/submit-form")
    public String submitForm(@RequestParam String companyName, @RequestParam String contactFace,
                             @RequestParam String phone, @RequestParam String email, Model model) {
        FormData formData = new FormData();
        formData.setCompanyName(companyName);
        formData.setContactFace(contactFace);
        formData.setPhone(phone);
        formData.setEmail(email);
        FormData savedFormData = formDataRepository.save(formData);

        // Start process in Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("companyName", companyName);
        variables.put("contactFace", contactFace);
        variables.put("phone", phone);
        variables.put("email", email);
        variables.put("formDataId", savedFormData.getId());
        runtimeService.startProcessInstanceByKey("partnerCooperation", variables);

        model.addAttribute("message", "Form submitted! Process started and data saved.");
        model.addAttribute("formDataId", savedFormData.getId());

        return "submitted";
    }

    @GetMapping("/download-pdf/{formDataId}")
    public ResponseEntity<byte[]> generateAndDownloadPdf(@PathVariable Long formDataId, Model model) {
        FormData formData = formDataRepository.findById(formDataId).orElse(null);

        if (formData != null) {
            byte[] pdfBytes = pdfGenerationService.generatePdf(formData);
            String companyName = formData.getCompanyName();
            String fileName = companyName.replaceAll("\\s", "_") + ".pdf";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } else {
            model.addAttribute("message", "Error: Form data not found for id: " + formDataId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
            @RequestParam("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String fileName = file.getOriginalFilename();

            emailService.sendEmail(fileName, fileBytes);

            return new ModelAndView("email-sent");

        } catch (Exception e) {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/emails/access-keyword")
    public String getEmailsWithAccessKeyword(Model model) throws MessagingException {
        model.addAttribute("emails", mailService.getEmailsWithAccessKeyword());
        return "emails/access-emails";
    }

    @GetMapping("/emails/decline-keyword")
    public String getEmailsWithDeclineKeyword(Model model) throws MessagingException {
        model.addAttribute("emails", mailService.getEmailsWithDeclineKeyword());
        return "emails/decline-emails";
    }

}
