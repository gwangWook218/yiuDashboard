package com.yiuDashboard.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SignupRequest {
    @NotBlank(message = "아이디를 입력하세요")
    private String loginId;

    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    private String passwordCheck;

    // STUDENT / PROFESSOR
    private String role;

    @JsonSetter("role")
    public void setRole(String raw) {
        this.role = normalizeRole(raw);
    }

    private static String normalizeRole(String raw) {
        if (raw == null || raw.isBlank()) return "STUDENT";
        String r = raw.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        switch (r) {
            case "STAFF":
            case "PROF":
            case "PROFESSOR": return "PROFESSOR";
            case "STUDENT":   return "STUDENT";
            case "ADMIN":     return "ADMIN";
            case "USER":      return "USER";
            default:          return "STUDENT";
        }
    }

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .email(this.email)
                .password(this.password)
                .role(List.of("ROLE_" + this.role))
                .build();
    }
}
