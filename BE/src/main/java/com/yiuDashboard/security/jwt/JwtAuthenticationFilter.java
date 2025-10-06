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
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ 로드맵 API는 토큰 없어도 통과 (가장 먼저 체크)
        if (path.startsWith("/api/learning-roadmap/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 0) 예비요청(OPTIONS)은 무조건 통과 (CORS / 도구 테스트 시 안전)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1) 인증 불필요한 경로는 필터 스킵 (✅ 스모크용 로드맵 API 포함)
        if (path.startsWith("/api/learning-roadmap/")
                || path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        // 3) 토큰이 없거나 Bearer 가 아니면 => 인증 시도하지 않고 통과
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }
        // 4) 토큰 파싱/검증 (오류가 나도 차단하지 말고 통과시킴: 스모크/퍼블릭 엔드포인트 보호)
        String token;
        try {
            token = authorization.split(" ")[1];
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        // 5) 만료된 토큰 => 통과(차단 X)
        try {
            if (jwtUtil.isExpired(token)) {
                // System.out.println("token expired");
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        // 6) 유효한 토큰인 경우에만 SecurityContext 설정
        try {
            String loginId = jwtUtil.getLoginId(token);
            String role    = jwtUtil.getRole(token);
            User user = new User();
            user.setLoginId(loginId);
            user.setPassword("임시 비밀번호");
            user.setRole(Role.valueOf(role));
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        // 7) 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
