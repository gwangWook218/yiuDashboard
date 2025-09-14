package com.yiuDashboard.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "ID를 입력하세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
    private String passwordCheck;

    private Role role;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
