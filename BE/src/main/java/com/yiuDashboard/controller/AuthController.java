package com.yiuDashboard.controller;

import com.yiuDashboard.entity.*;
import com.yiuDashboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ───────── 공통 검증 ─────────
    private ResponseEntity<?> validateRegisterRequest(SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었거나 형식이 잘못되었습니다.");
        }
        if (authService.checkLoginIdDuplicate(request.getLoginId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 ID가 존재합니다.");
        }
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
        return null;
    }

    // ───────── 중복 확인 (프론트: GET /api/auth/check-id?loginId=xxx) ─────────
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String loginId) {
        boolean exists = authService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // ───────── 회원가입 (프론트: POST /api/auth/signup) ─────────
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        var error = validateRegisterRequest(request, bindingResult);
        if (error != null) return error;

        // 1️⃣ 역할 표준화
        String role = normalizeRole(request.getRole());
        request.setRole(role);

        // 2️⃣ 허용된 역할만 통과
        Set<String> allowed = Set.of("STUDENT", "PROFESSOR", "ADMIN", "USER");
        if (!allowed.contains(role)) {
            return ResponseEntity.badRequest().body("올바른 회원 유형을 선택하세요.");
        }

        // 3️⃣ 회원 등록
        authService.register(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    // ───────── 기존 경로 호환 ─────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        return signup(request, bindingResult);
    }

    // ───────── 로그인 (JWT 없이 최소 정보만 반환) ─────────
    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();
        JwtToken jwtToken = authService.login(loginId, password);
        return jwtToken;
    }

    // ───────── 아이디 찾기 ─────────
    @PostMapping("/find-id")
    public ResponseEntity<?> findByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String loginId = authService.findLoginIdByEmail(email);
        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록된 이메일이 없습니다");
        }
        return ResponseEntity.ok(loginId);
    }

    // ───────── 역할 표준화 함수 ─────────
    private static String normalizeRole(String raw) {
        if (raw == null || raw.isBlank()) return "STUDENT";
        String r = raw.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        switch (r) {
            case "STAFF":      // 교직원 선택 시
            case "PROF":
            case "PROFESSOR":  return "PROFESSOR";
            case "STUDENT":    return "STUDENT";
            case "ADMIN":      return "ADMIN";
            case "USER":       return "USER";
            default:           return "STUDENT";
        }
    }
}
