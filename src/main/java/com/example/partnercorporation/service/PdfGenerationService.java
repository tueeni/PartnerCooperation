package com.example.partnercorporation.service;

import com.example.partnercorporation.entity.FormData;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(FormData formData) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Company Name: " + formData.getCompanyName()));
            document.add(new Paragraph("Contact Face: " + formData.getContactFace()));
            document.add(new Paragraph("Phone number: " + formData.getPhone()));
            document.add(new Paragraph("Email: " + formData.getEmail()));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
