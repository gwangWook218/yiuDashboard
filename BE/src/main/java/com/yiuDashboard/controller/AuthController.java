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

    @PostMapping("/register/sutdent")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        var errorResponse = validateRegisterRequest(request, bindingResult);
        if (errorResponse != null) return errorResponse;

        // 에러가 존재하지 않을 시 joinRequest 통해서 회원가입 완료
        request.setRole(Role.STUDENT);
        authService.register(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다");
    }

    @PostMapping("/register/professor")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        var errorResponse = validateRegisterRequest(request, bindingResult);
        if (errorResponse != null) return errorResponse;

        // 에러가 존재하지 않을 시 joinRequest 통해서 회원가입 완료
        request.setRole(Role.PROFESSOR);
        authService.register(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        var errorResponse = validateRegisterRequest(request, bindingResult);
        if (errorResponse != null) return errorResponse;

        // 에러가 존재하지 않을 시 joinRequest 통해서 회원가입 완료
        request.setRole(Role.ADMIN);
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

    @GetMapping("/login")
    public String userInfo(Authentication auth) {
        User user = authService.getLoginUserById(auth.getName());
        return "ID: " + user.getLoginId() + "\nrole: " + user.getRole();
    }
}
