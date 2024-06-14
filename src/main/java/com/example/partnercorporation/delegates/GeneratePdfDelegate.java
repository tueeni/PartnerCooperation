package com.example.partnercorporation.delegates;

import com.example.partnercorporation.entity.FormData;
import com.example.partnercorporation.repository.FormDataRepository;
import com.example.partnercorporation.service.PdfGenerationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneratePdfDelegate implements JavaDelegate {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @Autowired
    private FormDataRepository formDataRepository;
    @Override
    public void execute(DelegateExecution execution) {
        Long formDataId = (Long) execution.getVariable("formDataId");
        FormData formData = formDataRepository.findById(formDataId).orElse(null);
        String companyName = (String) execution.getVariable("companyName");


        if (formData != null) {
            // Generating form data
            byte[] pdfData = pdfGenerationService.generatePdf(formData);

            // saving pdf to process variable
            execution.setVariable(companyName, pdfData);
        } else {
            System.out.println("Form data not found for id: " + formDataId);
        }
    }
}
