package com.yiuDashboard.security.jwt;

import com.yiuDashboard.entity.Role;
import com.yiuDashboard.entity.User;
import com.yiuDashboard.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();

        // 로그인/회원가입은 필터 통과
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더가 없거나 Bearer 아닌 경우 → 인증 미설정으로 다음 필터
        // (공개 엔드포인트는 통과, 보호 엔드포인트는 이후 AccessDecision에서 401/403 처리)
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        // 만료 토큰 → 인증 미설정으로 통과 (EntryPoint/AccessDeniedHandler에서 처리)
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 유효 토큰 → SecurityContext에 인증 주입
        String loginId = jwtUtil.getLoginId(token);
        String roleStr = jwtUtil.getRole(token);

        User user = new User();
        user.setLoginId(loginId);
        user.setPassword("N/A"); // 매 요청 DB 조회 불필요하므로 임시값
        user.setRole(Role.valueOf(roleStr));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
