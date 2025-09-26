package com.yiuDashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemesterParser {
    public static void main(String[] args) {
        String pdfText = "..."; // PDF에서 뽑아온 텍스트

        String regex = "(\\d{4})년\\s*(\\d)학기[\\s\\S]*?학점\\s*:\\s*(\\d+)\\s*평점\\s*:\\s*([0-9.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pdfText);

        List<SemesterRecord> records = new ArrayList<>();

        while (matcher.find()) {
            String year = matcher.group(1);
            String semester = matcher.group(2);
            int credits = Integer.parseInt(matcher.group(3));
            double gpa = Double.parseDouble(matcher.group(4));

            records.add(new SemesterRecord(year + "-" + semester, credits, gpa));
        }

        records.forEach(System.out::println);
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