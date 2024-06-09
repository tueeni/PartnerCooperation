package com.example.partnercorporation.controller;


import com.example.partnercorporation.entity.FormData;
import com.example.partnercorporation.repository.FormDataRepository;
import com.example.partnercorporation.service.PdfGenerationService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        runtimeService.startProcessInstanceByKey("helloWorldProcess", variables);

        model.addAttribute("message", "Form submitted! Process started and data saved.");
        model.addAttribute("formDataId", savedFormData.getId());

        return "submitted";
    }

    @GetMapping("/generate-pdf/{formDataId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long formDataId, Model model) {
        FormData formData = formDataRepository.findById(formDataId).orElse(null);

        if (formData != null) {
            byte[] pdfBytes = pdfGenerationService.generatePdf(formData);
            String companyName = formData.getCompanyName();
            String fileName = companyName.replaceAll("\\s", "_") + "_data.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(fileName, fileName);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } else {
            // exception
            model.addAttribute("message", "Error: Form data not found for id: " + formDataId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/accept/{id}")
    public ModelAndView acceptFormData(@PathVariable Long id) {
        FormData formData = formDataRepository.findById(id).orElseThrow(() -> new RuntimeException("FormData not found"));
        formData.setStatus(true);
        formDataRepository.save(formData);
        return new ModelAndView("accepted");  // Ensure "accepted" corresponds to a valid Thymeleaf template
    }

    @PutMapping("/reject/{id}")
    public ModelAndView rejectFormData(@PathVariable Long id) {
        FormData formData = formDataRepository.findById(id).orElseThrow(() -> new RuntimeException("FormData not found"));
        formData.setStatus(false);
        formDataRepository.save(formData);
        return new ModelAndView("rejected");  // Ensure "rejected" corresponds to a valid Thymeleaf template
    }

}
