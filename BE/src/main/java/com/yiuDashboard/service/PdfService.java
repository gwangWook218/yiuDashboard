package com.yiuDashboard.service;

import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

    public List<SemesterRecord> extractSemesterRecords(MultipartFile file, Long userId) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());

        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);
        document.close();

        // 정규식으로 학기별 학점/평점 추출
        String regex = "(\\d{4})년\\s*(\\d)학기[\\s\\S]*?학점\\s*:\\s*(\\d+)\\s*평점\\s*:\\s*([0-9.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pdfText);

        List<SemesterRecord> records = new ArrayList<>();

        while (matcher.find()) {
            String year = matcher.group(1);
            String semester = matcher.group(2);
            int credits = Integer.parseInt(matcher.group(3));
            double gpa = Double.parseDouble(matcher.group(4));

            records.add(new SemesterRecord(year + "-" + semester, credits, gpa, userId));
        }

        return records;
    }

//    public List<CreditProgress> extractCreditProgress(String filePath) throws IOException {
//        File file = new File(filePath);
//        PDDocument document = PDDocument.load(file);
//
//        PDFTextStripper stripper = new PDFTextStripper();
//        String pdfText = stripper.getText(document);
//        document.close();
//
//        String regex = "교양필수\\s+(\\d+)\\s+(\\d+)|"
//                + "교양(\\d+)\\s+(\\d+)|"
//                + "기초전공\\s+(\\d+)\\s+(\\d+)|"
//                + "최소전공이수학점\\s+(\\d+)\\s+(\\d+)|"
//                + "졸업학점\\s+(\\d+).*취득학점\\s+(\\d+).*계\\s+(\\d+)";
//
//        return regex;
//    }
}
