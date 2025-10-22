package com.yiuDashboard.controller;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import com.yiuDashboard.repository.CreditProgressRepository;
import com.yiuDashboard.repository.SemesterRecordRepository;
import com.yiuDashboard.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final PdfService service;
    private final SemesterRecordRepository recordRepository;
    private final CreditProgressRepository progressRepository;

    /** ===== 학기별 성적 PDF 업로드 =====
     *  데모 우선: 기본은 저장 안 함(save=false). 저장 원하면 쿼리파라미터로 save=true 추가.
     *  예) POST /api/grades/semester/upload?save=true
     */
    @PostMapping(
            value = "/semester/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadSemesterRecord(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam(name = "save", defaultValue = "false") boolean save
    ) {
        if (file == null || file.isEmpty()) return ResponseEntity.badRequest().body("PDF 파일이 비어있습니다.");
        try {
            List<SemesterRecord> records = service.extractSemesterRecords(file, userId);
            if (CollectionUtils.isEmpty(records)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("PDF에서 학기별 성적을 읽지 못했습니다.");
            }

            if (save) {
                try {
                    recordRepository.saveAll(records); // 외래키 없으면 여기서 실패
                } catch (DataIntegrityViolationException fk) {
                    // 데모 모드 우선: 저장은 건너뛰고 파싱 결과만 반환
                    return ResponseEntity.ok(records);
                }
            }
            return ResponseEntity.ok(records);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PDF 처리 중 오류: " + e.getMessage());
        }
    }

    @GetMapping(value = "/semester/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SemesterRecord>> getSemesterRecords(@PathVariable Long userId) {
        return ResponseEntity.ok(recordRepository.findByUserId(userId));
    }

    /** ===== 이수구분(학점 진행) PDF 업로드 =====
     *  동일하게 저장은 기본 off. 저장 원하면 ?save=true
     */
    @PostMapping(
            value = "/credit-progress/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadCreditProgress(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam(name = "save", defaultValue = "false") boolean save
    ) {
        if (file == null || file.isEmpty()) return ResponseEntity.badRequest().body("PDF 파일이 비어있습니다.");
        try {
            List<CreditProgress> progresses = service.extractCreditProgress(file, userId);
            if (CollectionUtils.isEmpty(progresses)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("PDF에서 이수구분 정보를 읽지 못했습니다.");
            }

            if (save) {
                try {
                    progressRepository.saveAll(progresses);
                } catch (DataIntegrityViolationException fk) {
                    return ResponseEntity.ok(progresses);
                }
            }
            return ResponseEntity.ok(progresses);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PDF 처리 중 오류: " + e.getMessage());
        }
    }

    @GetMapping(value = "/credit-progress/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreditProgress>> getCreditProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(progressRepository.findByUserId(userId));
    }
}
