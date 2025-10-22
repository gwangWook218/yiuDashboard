package com.yiuDashboard.service;

import com.yiuDashboard.entity.JwtToken;
import com.yiuDashboard.entity.SignupRequest;
import com.yiuDashboard.entity.User;
import com.yiuDashboard.repository.UserRepository;
import com.yiuDashboard.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public void register(SignupRequest signupRequest) {
        if (userRepository.existsByLoginId(signupRequest.getLoginId())) {
            return;
        }

        // 비밀번호 해시
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

        // role 표준화 (ROLE_ 제거, 대문자, 허용값 외 기본 STUDENT)
        String normalizedRole = normalizeRole(signupRequest.getRole());

        // DTO -> Entity 변환
        User user = User.builder()
                .loginId(signupRequest.getLoginId())
                .email(signupRequest.getEmail())
                .password(encodedPassword)
                .role(List.of("ROLE_" + normalizedRole))
                .build();

        // 저장
        userRepository.save(user);
    }

    public JwtToken login(String loginId, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtUtil.generateToken(authentication);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String role = authorities.contains("PROFESSOR") ? "PROFESSOR" : "STUDENT";

        return JwtToken.builder()
                .grantType(jwtToken.getGrantType())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .loginId(loginId)
                .role(role)
                .build();
    }

    public String findLoginIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getLoginId)
                .orElse(null);
    }

    // ===== role 표준화 =====
    private static String normalizeRole(String raw) {
        if (raw == null || raw.isBlank()) return "STUDENT";     // 기본값
        String r = raw.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);          // ROLE_STUDENT -> STUDENT
        return switch (r) {
            case "STAFF", "PROF", "PROFESSOR" -> "PROFESSOR";
            case "ADMIN" -> "ADMIN";
            case "STUDENT" -> "STUDENT";
            default -> "STUDENT";
        };
    }
}
