package com.yiuDashboard.service;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

    private String extractNormalizedText(MultipartFile file) throws IOException {
        try (PDDocument doc = PDDocument.load(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String raw = stripper.getText(doc);
            if (raw == null) return "";
            raw = raw.replace("\r", "")
                    .replace("\u00A0", " ")
                    .replaceAll("\\s*──────────────────────────────\\s*", "\n")
                    .replaceAll(" \t{2,}", " ")
                    .trim();
            if (raw.contains(",")) {
                char dec = DecimalFormatSymbols.getInstance().getDecimalSeparator();
                if (dec == '.') raw = raw.replace(',', '.');
            }
            return raw;
        }
    }

    public List<SemesterRecord> extractSemesterRecords(MultipartFile file, Long userId) throws IOException {
        String text = extractNormalizedText(file);
        if (text.isBlank()) return Collections.emptyList();

        Pattern ko = Pattern.compile(
                "(\\d{4})년\\s*(\\d)학기[\\s\\S]*?학점\\s*:\\s*(\\d+)\\s*평점\\s*:\\s*([0-9.]+)",
                Pattern.MULTILINE
        );
        Pattern en = Pattern.compile(
                "(?:Semester|Term)\\s*:\\s*([1-4])\\s*[-~]\\s*([1-2])[\\s\\S]*?(?:Credits|Units)\\s*:\\s*([0-9]+)[^\\d]+(?:GPA|Average)\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );

        List<SemesterRecord> out = new ArrayList<>();
        Matcher m = ko.matcher(text);
        while (m.find()) {
            int year = Integer.parseInt(m.group(1));
            int half = Integer.parseInt(m.group(2));
            int credits = Integer.parseInt(m.group(3));
            double gpa = Double.parseDouble(m.group(4));
            out.add(new SemesterRecord(year + "-" + half, credits, gpa, userId));
        }
        if (out.isEmpty()) {
            Matcher m2 = en.matcher(text);
            while (m2.find()) {
                int year = Integer.parseInt(m2.group(1));
                int half = Integer.parseInt(m2.group(2));
                int credits = Integer.parseInt(m2.group(3));
                double gpa = Double.parseDouble(m2.group(4));
                out.add(new SemesterRecord(year + "-" + half, credits, gpa, userId));
            }
        }

        try {
            out.sort(Comparator.comparingInt(this::inferOrderSafely));
        } catch (Exception ignore) {}

        return out;
    }

    public List<CreditProgress> extractCreditProgress(MultipartFile file, Long userId) throws IOException {
        String text = extractNormalizedText(file);

        Map<String, Integer> requiredMap = new HashMap<>();
        Map<String, Integer> earnedMap = new HashMap<>();

        Matcher m1 = Pattern.compile("(?<!복수,부,연계전공 )(교양필수|기초전공)\\s+(\\d+)\\s+(\\d+)").matcher(text);
        while (m1.find()) {
            requiredMap.put(m1.group(1), Integer.parseInt(m1.group(2)));
            earnedMap.put(m1.group(1), Integer.parseInt(m1.group(3)));
        }

        Matcher m2 = Pattern.compile("교양선택\\s*(\\d+)(?:\\s|$)").matcher(text);
        if (m2.find()) requiredMap.put("교양선택", Integer.parseInt(m2.group(1)));

        Matcher m3 = Pattern.compile("교양[1-7]\\s*(\\d+)").matcher(text);
        int liberalEarned = 0;
        while (m3.find()) liberalEarned += Integer.parseInt(m3.group(1));
        earnedMap.put("교양선택", liberalEarned);

        Matcher m4 = Pattern.compile("(?s)단일전공자.*?최소전공이수학점\\s*(\\d+)\\s*(\\d+)").matcher(text);
        int majorRequired = 0, majorEarned = 0;
        if (m4.find()) {
            majorRequired = Integer.parseInt(m4.group(1));
            majorEarned = Integer.parseInt(m4.group(2));
        }
        requiredMap.put("전공", majorRequired);
        earnedMap.put("전공", majorEarned);

        Matcher m5 = Pattern.compile("졸업학점\\s*(\\d+)").matcher(text);
        if (m5.find()) requiredMap.put("졸업학점", Integer.parseInt(m5.group(1)));
        Matcher m6 = Pattern.compile("취득학점\\s*(\\d+)").matcher(text);
        if (m6.find()) earnedMap.put("졸업학점", Integer.parseInt(m6.group(1)));

        List<CreditProgress> list = new ArrayList<>();
        for (String k : requiredMap.keySet()) {
            list.add(new CreditProgress(k, requiredMap.getOrDefault(k, 0), earnedMap.getOrDefault(k, 0), userId));
        }
        return list;
    }

    private int inferOrderSafely(SemesterRecord r) {
        Integer order = tryGetInt(r, "getSemesterOrder", "getOrder", "getIdx", "getIndex");
        if (order != null) return order;
        String key = tryGetString(r, "getSemesterKey", "getSemester", "getTermKey", "getTerm");
        if (key != null) {
            int parsed = parseOrderFromKey(key);
            if (parsed > 0) return parsed;
        }
        Integer y = tryGetInt(r, "getGradeYear", "getYear", "getGrade");
        Integer h = tryGetInt(r, "getSemesterHalf", "getHalf", "getSemesterNo", "getSemesterNum");
        if (y != null && h != null) return (y - 1) * 2 + h;
        return Integer.MAX_VALUE;
    }

    private int parseOrderFromKey(String key) {
        try {
            String[] p = key.split("[^0-9]+");
            List<Integer> nums = new ArrayList<>();
            for (String s : p) {
                if (s == null || s.isBlank()) continue;
                nums.add(Integer.parseInt(s));
            }
            if (nums.size() >= 2) {
                int y = nums.get(0);
                int h = nums.get(1);
                if (1 <= y && y <= 4 && (h == 1 || h == 2)) return (y - 1) * 2 + h;
            }
        } catch (Exception ignore) {}
        return -1;
    }

    private Integer tryGetInt(Object obj, String... methodNames) {
        for (String name : methodNames) {
            try {
                Method m = obj.getClass().getMethod(name);
                Object v = m.invoke(obj);
                if (v instanceof Number) return ((Number) v).intValue();
                if (v != null) return Integer.parseInt(String.valueOf(v));
            } catch (Exception ignore) {}
        }
        return null;
    }

    private String tryGetString(Object obj, String... methodNames) {
        for (String name : methodNames) {
            try {
                Method m = obj.getClass().getMethod(name);
                Object v = m.invoke(obj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignore) {}
        }
        return null;
    }
}
