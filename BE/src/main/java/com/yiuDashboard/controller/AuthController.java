package com.yiuDashboard.controller;

import com.yiuDashboard.entity.*;
import com.yiuDashboard.security.jwt.JwtUtil;
import com.yiuDashboard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    private ResponseEntity<?> validateRegisterRequest(RegisterRequest request, BindingResult bindingResult) {

        // Validation 에노테이션 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었거나 형식이 잘못되었습니다.");
        }

        // ID 중복 여부 확인
        if (authService.checkLoginIdDuplicate(request.getLoginId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 ID가 존재합니다.");
        }

        // 비밀번호 = 비밀번호 체크 여부 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        var errorResponse = validateRegisterRequest(request, bindingResult);
        if (errorResponse != null) return errorResponse;

        if (request.getRole() == null ||
                (!request.getRole().equals(Role.STUDENT) &&
                        !request.getRole().equals(Role.PROFESSOR) &&
                        !request.getRole().equals(Role.ADMIN))) {
            return ResponseEntity.badRequest().body("올바른 회원 유형을 선택하세요.");
        }
        authService.register(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = authService.login(loginRequest);

        if (user == null) {
            return ResponseEntity.badRequest().body("ID 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createJwt(user.getLoginId(), user.getRole().name(), 1000 * 60 * 60L);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String loginId = authService.findLoginIdByEmail(email);

        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("등록된 이메일이 없습니다");
        }

        return ResponseEntity.ok(loginId);
    }
}
