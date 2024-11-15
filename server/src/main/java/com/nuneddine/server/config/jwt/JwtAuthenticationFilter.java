package com.nuneddine.server.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 예외 경로 확인
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/cors") || path.startsWith("/api/v1/cors/") || path.startsWith("/api/v1/oauth/kakao/")
            || path.startsWith("/swagger-ui/") || path.startsWith("/api/v1/map/{mapNumber}") || path.startsWith("/api/v1/snowman/{snowmanId}")) {
            // 지정된 경로는 필터 적용 없이 바로 다음 필터로 넘어감
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {
                Long id = jwtUtil.extractMemberId(token); // 사용자 ID 추출

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(id, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))); // 사용자 ID를 Principal로 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
