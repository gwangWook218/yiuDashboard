package com.yiuDashboard.controller;

import com.yiuDashboard.dto.SemesterRecord;
import com.yiuDashboard.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final PdfService service;

    @GetMapping("/semesters")
    public List<SemesterRecord> getSemesters() throws IOException {
        String filePath = "C:/Users/안광욱/Downloads/용인대학교 학기별.pdf";
        return service.extractSemesterRecords(filePath);
    }
}
