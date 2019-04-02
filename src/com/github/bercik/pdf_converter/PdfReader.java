package com.github.bercik.pdf_converter;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {
    public String readTextFromPdf(@SuppressWarnings("SameParameterValue") String filepath) throws IOException {
        PDDocument document = null;
        try {
            File file = new File(filepath);
            document = PDDocument.load(file);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            return pdfTextStripper.getText(document);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
