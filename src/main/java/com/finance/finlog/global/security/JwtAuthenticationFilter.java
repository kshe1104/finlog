package com.finance.finlog.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 토큰이 없으면, 예외를 던지는 게 아니라 다음 필터로 넘김
    // 인증이 필요한 API인지 여부는 SecurityConfig 에서 결정
    // 토큰이 있으면 인증정보를 채워주기만 하는 역할
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String token = extractToken(request); // 헤더에서 토큰을 추출

        if(StringUtils.hasText(token)&& jwtTokenProvider.validateToken(token)){ // 토큰을 갖고있고, 토큰이 유효하다면
            Long userId = jwtTokenProvider.getUserId(token);  //userID 저장

            // SpringSecurity는 인증정보를 Authentication로 다루는데 UsernamePasswordAuthenticationToken은 그 구현체임.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
