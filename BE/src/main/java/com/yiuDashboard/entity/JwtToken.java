package com.yiuDashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String loginId;
    private String role;
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
