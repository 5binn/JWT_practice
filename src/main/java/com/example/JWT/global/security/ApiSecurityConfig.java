package com.example.JWT.global.security;

import com.example.JWT.global.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .requestMatchers("/api/*/members/logout").permitAll() // 로그인은 누구나 가능
//                                .requestMatchers(HttpMethod.POST,"/api/*/members/login").permitAll() // 로그인은 누구나 가능
                        .requestMatchers("/api/*/articles").permitAll() // 전체 글 보기는 누구나 가능
                        .requestMatchers("/api/*/articles/*").permitAll() // 글 상세보기는 누나 가능
                        .anyRequest().authenticated()) // 나머지는 인증/인가 처리된 사용자만 가능
                .cors((cors) -> cors
                        .disable())
                .csrf((csrf) -> csrf
                        .disable())
                .httpBasic((httpBasic) -> httpBasic //선택
                        .disable())
                .formLogin((formLogin) -> formLogin //선택
                        .disable())
                .sessionManagement((sessionManagement) -> sessionManagement //필수
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore( //필수
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
        ;
        return http.build();
    }
}