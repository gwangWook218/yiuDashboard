package com.yiuDashboard.entity;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // "ROLE_STUDENT" / "ROLE_PROFESSOR" / "ROLE_ADMIN"
}
