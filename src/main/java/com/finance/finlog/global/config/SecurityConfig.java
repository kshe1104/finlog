package com.finance.finlog.global.config;

import com.finance.finlog.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RedisService redisService) throws Exception{
        http
                // CSRF 비활성화 , 세션이 아닌 토큰을 사용하기 때문에 CSRF 공격 불가능
                .csrf(AbstractHttpConfigurer::disable)

                // 세션 사용 안함(JWT 사용)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청별 인증 설정
                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "/oauth2/**",
                        "/login/**",
                        "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                        "view/**"
                )
                        .permitAll() // 위 목록에 적힌 주소는 누구나 접근 가능(허용)
                        .anyRequest().authenticated()) // 그 외 나머지 모든 주소는 '로그인 인증' 이 필수!

                // OAuth2 로그인 설정
                .oauth2Login(oauth2->oauth2.userInfoEndpoint(userInfo->userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler))

                // JWT 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,redisService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
