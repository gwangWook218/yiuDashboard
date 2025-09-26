package com.yiuDashboard;

import com.yiuDashboard.dto.CreditProgress;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfReaderExample {
    public static void main(String[] args) throws IOException {

        File file = new File("C:/Users/안광욱/Downloads/용인대학교 이수구분별.pdf");
        PDDocument document = PDDocument.load(file);

        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);
        System.out.println(pdfText);
        document.close();

        String regex = "교양필수\\s*(\\d+)\\s*(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pdfText);

        List<CreditProgress> records = new ArrayList<>();

        while (matcher.find()) {
            String category = matcher.group(1);
            int completed = Integer.parseInt(matcher.group(2));
            int total = Integer.parseInt(matcher.group(3));

            records.add(new CreditProgress(category, completed, total));
        }

        records.forEach(System.out::println);
    }
}
