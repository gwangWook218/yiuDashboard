package com.yiuDashboard.service;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<CreditProgress> extractCreditProgress(MultipartFile file, Long userId) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);
        document.close();

        pdfText = pdfText.replace("\u00A0", " "); // non-breaking space
        pdfText = pdfText.replaceAll("\\r?\\n", " "); // 줄바꿈을 공백으로

        Map<String, Integer> requiredMap = new HashMap<>();
        Map<String, Integer> earnedMap = new HashMap<>();

        // 교양필수, 기초전공
        Pattern basicPattern = Pattern.compile("(?<!복수,부,연계전공 )(교양필수|기초전공)\\s*(\\d+)\\s*(\\d+)");
        Matcher m1 = basicPattern.matcher(pdfText);
        while (m1.find()) {
            requiredMap.put(m1.group(1), Integer.parseInt(m1.group(2)));
            earnedMap.put(m1.group(1), Integer.parseInt(m1.group(3)));
        }

        // 교양선택 이수기준
        Pattern liberalReqPattern = Pattern.compile("교양선택\\s*(\\d+)(?:\\s|$)");
        Matcher m2 = liberalReqPattern.matcher(pdfText);
        if (m2.find()) {
            requiredMap.put("교양선택", Integer.parseInt(m2.group(1)));
        }

        // 교양1~7 취득 합계
        Pattern liberalEarnedPattern = Pattern.compile("교양[1-7]\\s*(\\d+)");
        Matcher m3 = liberalEarnedPattern.matcher(pdfText);
        int liberalEarned = 0;
        while (m3.find()) {
            liberalEarned += Integer.parseInt(m3.group(1));
        }
        earnedMap.put("교양선택", liberalEarned);

        // 전공 (단일 + 복수/부/연계)
        Pattern majorPattern = Pattern.compile("(?s)단일전공자.*?최소전공이수학점\\s*(\\d+)\\s*(\\d+)");
        Matcher m4 = majorPattern.matcher(pdfText);
        int majorRequired = 0;
        int majorEarned = 0;
        if (m4.find()) {
            majorRequired = Integer.parseInt(m4.group(1));
            majorEarned = Integer.parseInt(m4.group(2));
        }
        requiredMap.put("전공", majorRequired);
        earnedMap.put("전공", majorEarned);

        // 졸업학점 / 취득학점
        Pattern gradPattern = Pattern.compile("졸업학점\\s*(\\d+)");
        Matcher m5 = gradPattern.matcher(pdfText);
        if (m5.find()) {
            requiredMap.put("졸업학점", Integer.parseInt(m5.group(1)));
        }

        Pattern gradEarnedPattern = Pattern.compile("취득학점\\s*(\\d+)");
        Matcher m6 = gradEarnedPattern.matcher(pdfText);
        if (m6.find()) {
            earnedMap.put("졸업학점", Integer.parseInt(m6.group(1)));
        }

        List<CreditProgress> progresses = new ArrayList<>();
        for (String category : requiredMap.keySet()) {
            int required = requiredMap.getOrDefault(category, 0);
            int earned = earnedMap.getOrDefault(category, 0);
            progresses.add(new CreditProgress(category, required, earned, userId));
        }

        return progresses;
    }
}
