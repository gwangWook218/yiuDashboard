package com.yiuDashboard.controller;

import com.yiuDashboard.entity.User;
import com.yiuDashboard.entity.personalGrades.CreditProgress;
import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import com.yiuDashboard.entity.personalGrades.StudentAdditionalInfo;
import com.yiuDashboard.repository.CreditProgressRepository;
import com.yiuDashboard.repository.SemesterRecordRepository;
import com.yiuDashboard.repository.StudentAdditionalInfoRepository;
import com.yiuDashboard.repository.UserRepository;
import com.yiuDashboard.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final PdfService service;
    private final UserRepository userRepository;
    private final SemesterRecordRepository recordRepository;
    private final CreditProgressRepository progressRepository;
    private final StudentAdditionalInfoRepository studentAdditionalInfoRepository;

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
        User user = userRepository.findByLoginId(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));
        try {
            List<SemesterRecord> records = service.extractSemesterRecords(file, user);
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

    @GetMapping(value = "/semester", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SemesterRecord>> getSemesterRecords(@RequestParam String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(recordRepository.findByUserId(user.getId()));
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
        User user = userRepository.findByLoginId(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));
        try {
            List<CreditProgress> progresses = service.extractCreditProgress(file, user);
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

    @GetMapping(value = "/credit-progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreditProgress>> getCreditProgress(@RequestParam String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(progressRepository.findByUserId(user.getId()));
    }

    @PostMapping("/student-info/save")
    public StudentAdditionalInfo saveStudentInfo(@RequestBody StudentAdditionalInfo info, Authentication authentication) {

        // 로그인 유저 조회
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));;
        if (user == null) {
            throw new IllegalArgumentException("로그인 유저가 DB에 존재하지 않습니다.");
        }

        // 엔티티에 로그인 유저 자동 세팅
        info.setUser(user);

        return studentAdditionalInfoRepository.save(info);
    }

    @GetMapping("/my-info-add")
    public ResponseEntity<?> getStudentInfo(Authentication authentication) {
        // 로그인 유저 조회
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 loginId의 사용자를 찾을 수 없습니다."));;

        if (user == null) {
            throw new IllegalArgumentException("로그인 유저가 DB에 존재하지 않습니다.");
        }

        // 로그인 유저의 StudentAdditionalInfo 조회
        StudentAdditionalInfo info = studentAdditionalInfoRepository.findByUserId(user.getId());
        if (info == null) {
            // 기본값
            info = new StudentAdditionalInfo();
            info.setTransferCredits(0);
            info.setThesisStatus(StudentAdditionalInfo.GraduationThesisStatus.제출필요);
            info.setToeicScore(null);
            info.setCertificateStatus(StudentAdditionalInfo.CertificateStatus.미취득);
            info.setUser(user);
        }

        // 프론트 친화적 변환
        Map<String, Object> dto = new HashMap<>();
        dto.put("transferCredits", info.getTransferCredits());
        dto.put("thesisStatus", info.getThesisStatus() == null ? "제출필요" : (info.getThesisStatus() == StudentAdditionalInfo.GraduationThesisStatus.제출필요 ? "제출필요" : "제출함"));
        dto.put("toeicScore", info.getToeicScore());
        dto.put("certificateStatus", info.getCertificateStatus() == null ? "미취득" : (info.getCertificateStatus() == StudentAdditionalInfo.CertificateStatus.미취득 ? "미취득" : "취득"));

        return ResponseEntity.ok(dto);
    }
}
