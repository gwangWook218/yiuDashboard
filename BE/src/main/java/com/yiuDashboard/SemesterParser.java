package com.yiuDashboard;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemesterParser {
    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/안광욱/Downloads/용인대학교 이수구분별.pdf");
        PDDocument document = PDDocument.load(file);

        // 텍스트 추출
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String pdfText = pdfStripper.getText(document);
        System.out.println(pdfText);

//        String regex = "(\\d{4})년\\s*(\\d)학기[\\s\\S]*?학점\\s*:\\s*(\\d+)\\s*평점\\s*:\\s*([0-9.]+)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(pdfText);
//
//        List<SemesterRecord> records = new ArrayList<>();
//
//        while (matcher.find()) {
//            String year = matcher.group(1);
//            String semester = matcher.group(2);
//            int credits = Integer.parseInt(matcher.group(3));
//            double gpa = Double.parseDouble(matcher.group(4));
//
//            records.add(new SemesterRecord(year + "-" + semester, credits, gpa));
//        }
//
//        records.forEach(System.out::println);
    }
}

class SemesterRecord {
    private String yearSemester;
    private int credits;
    private double gpa;

    public SemesterRecord(String yearSemester, int credits, double gpa) {
        this.yearSemester = yearSemester;
        this.credits = credits;
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return yearSemester + " | 이수학점: " + credits + " | 평점: " + gpa;
    }
}