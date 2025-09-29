package com.yiuDashboard.controller;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import com.yiuDashboard.repository.CreditProgressRepository;
import com.yiuDashboard.repository.SemesterRecordRepository;
import com.yiuDashboard.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final PdfService service;
    private final SemesterRecordRepository recordRepository;
    private final CreditProgressRepository progressRepository;

    @PostMapping("/semester/upload")
    public ResponseEntity<List<SemesterRecord>> uploadSemesterRecord(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) throws IOException {
        List<SemesterRecord> records = service.extractSemesterRecords(file, userId);
        recordRepository.saveAll(records);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/semester/{userId}")
    public ResponseEntity<List<SemesterRecord>> getSemesterRecords(@PathVariable Long userId) {
        return ResponseEntity.ok(recordRepository.findByUserId(userId));
    }

    @PostMapping("/credit-progress/upload")
    public ResponseEntity<List<CreditProgress>> uploadCreditProgress(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) throws IOException {
        List<CreditProgress> progresses = service.extractCreditProgress(file,userId);
        progressRepository.saveAll(progresses);
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/credit-progress/{userId}")
    public ResponseEntity<List<CreditProgress>> getCreditProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(progressRepository.findByUserId(userId));
    }
}
