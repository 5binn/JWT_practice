package com.example.JWT.global.rq;

import com.example.JWT.domain.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final EntityManager entityManager;
    private Member member;

    public void setCrossDomainCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("home")
                .secure(true)
                .httpOnly(true)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }


}
