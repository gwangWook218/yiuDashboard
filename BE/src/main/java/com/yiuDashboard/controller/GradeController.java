package com.yiuDashboard.controller;

import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import com.yiuDashboard.repository.PersonalGradesRepository;
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
    private final PersonalGradesRepository repository;

    @PostMapping("/upload")
    public ResponseEntity<List<SemesterRecord>> uploadSemesterRecord(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) throws IOException {
        List<SemesterRecord> records = service.extractSemesterRecords(file, userId);
        repository.saveAll(records);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<SemesterRecord>> getSemesterRecords(@PathVariable Long userId) {
        return ResponseEntity.ok(repository.findByUserId(userId));
    }
}
