package com.github.bercik;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

class PdfReader {
    String readTextFrom(@SuppressWarnings("SameParameterValue") String filepath) throws IOException {
        File file = new File(filepath);
        PDDocument document = PDDocument.load(file);

        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        return pdfTextStripper.getText(document);
    }

}
