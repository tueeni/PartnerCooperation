package com.example.partnercorporation.controller;


import com.example.partnercorporation.entity.FormData;
import com.example.partnercorporation.repository.FormDataRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormDataRepository formDataRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @PostMapping("/submit-form")
    @ResponseBody
    public String submitForm(@RequestParam String companyName, @RequestParam String contactFace, @RequestParam String phone, @RequestParam String email) {
        FormData formData = new FormData();
        formData.setCompanyName(companyName);
        formData.setContactFace(contactFace);
        formData.setPhone(phone);
        formData.setEmail(email);
        formDataRepository.save(formData);

        // Start process in Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("companyName", companyName);
        variables.put("contactFace", contactFace);
        variables.put("phone", phone);
        variables.put("email", email);
        runtimeService.startProcessInstanceByKey("helloWorldProcess", variables);

        return "Form submitted! Process started and data saved.";
    }
}
