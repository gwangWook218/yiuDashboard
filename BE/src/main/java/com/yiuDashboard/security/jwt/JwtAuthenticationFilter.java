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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 * - 퍼블릭/허용 경로는 shouldNotFilter()로 완전히 스킵
 * - Authorization 헤더가 없거나 Bearer 형식이 아니면 그냥 통과(403/401 만들지 않음)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // ✅ 필터 적용을 건너뛸 경로 목록
    private static final List<RequestMatcher> SKIP = List.of(
            new AntPathRequestMatcher("/api/public/**"),
            new AntPathRequestMatcher("/api/auth/login"),
            new AntPathRequestMatcher("/api/auth/register/**"),
            // ✅ 로컬 스모크용: 전임교원 API GET은 인증 없이 통과
            new AntPathRequestMatcher("/api/faculty/**", "GET"),
            new AntPathRequestMatcher("/api/faculty-condition/**", "GET")

    );

    /** 해당 요청을 이 필터가 처리하지 않도록 스킵할지 여부 */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (RequestMatcher m : SKIP) {
            if (m.matches(request)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1) Authorization 헤더 확인 (없으면 그냥 다음 필터로)
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        // 2) 토큰 파싱
        String token = authorization.split(" ")[1];

        // 3) 만료 토큰이면 통과(여기서 차단하지 않음 → 401/403는 컨트롤러/핸들러에서)
        if (jwtUtil.isExpired(token)) {
            // System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 4) 토큰에서 사용자 정보 추출 (프로젝트에 맞춘 기존 방식 유지)
        String loginId = jwtUtil.getLoginId(token);
        String role = jwtUtil.getRole(token);

        // 임시 User 구성 (DB 재조회 없이)
        User user = new User();
        user.setLoginId(loginId);
        user.setPassword("임시 비밀번호"); // 실제 비밀번호 필요 없음
        user.setRole(Role.valueOf(role));

        // UserDetails 생성 및 인증 컨텍스트 주입
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5) 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
