// LocalSecurityConfig.java
package com.yiuDashboard.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.annotation.Order;

@Configuration
@Profile("local")                 // 로컬에서만 활성화
public class LocalSecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain localChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")               // 모든 요청에 이 체인 적용
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .headers(h -> h.frameOptions(frame -> frame.disable())); // 콘솔/iframe 등 편의
        return http.build();
    }
}
