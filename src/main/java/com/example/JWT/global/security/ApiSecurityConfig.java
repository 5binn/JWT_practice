package com.example.JWT.global.security;

import com.example.JWT.global.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .authorizeRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/api/*/members/login").permitAll() // 로그인은 누구나 가능
                        .requestMatchers("/api/*/members/logout").permitAll() // 로그아웃은 누구나 가능
//                                .requestMatchers(HttpMethod.POST,"/api/*/members/login").permitAll() // 로그인은 누구나 가능
                        .requestMatchers("/api/*/articles").permitAll() // 전체 글 보기는 누구나 가능
                        .requestMatchers("/api/*/articles/*").permitAll() // 글 상세보기는 누나 가능
                        .anyRequest().authenticated()) // 나머지는 인증/인가 처리된 사용자만 가능
                .cors(
                        cors -> cors.disable()
                ) // cors 설정, 타 도메인에서 api 호출 가능
                .csrf(
                        csrf -> csrf.disable()
                ) // csrf 토큰 끄기
                .httpBasic(
                        httpBasic -> httpBasic.disable()
                ) // httpBasic 로그인 방식 끄기
                .formLogin(
                        formLogin -> formLogin.disable()
                ) // 폼 로그인 방식 끄기
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS)
                ) // 세션 끄기
                .addFilterBefore(
                        jwtAuthorizationFilter, //엑세스 토큰을 이용한 로그인 처리
                        UsernamePasswordAuthenticationFilter.class
                )
        ;
        return http.build();
    }
}