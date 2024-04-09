package com.example.JWT.domain.member.service;

import com.example.JWT.domain.member.entity.Member;
import com.example.JWT.domain.member.repository.MemberRepository;
import com.example.JWT.global.exception.GlobalException;
import com.example.JWT.global.jwt.JwtProvider;
import com.example.JWT.global.rsData.RsData;
import com.example.JWT.global.security.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Member join(String username, String password, String email) {
        Member member = Member.builder().
                username(username).
                password(password).
                email(email).
                build();
        String refreshToken = jwtProvider.generateRefreshToken(member);

        member.setRefreshToken(refreshToken);

        memberRepository.save(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }


    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUser(
                id,
                username,
                "",
                authorities
        );
    }

    public boolean validateToken(String token) {
        return jwtProvider.verify(token);
    }
    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 리프레시 토큰입니다."));

        String accessToken = jwtProvider.generateAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);
    }
    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokenResponse {
        private Member member;
        private String accessToken;
        private String refreshToken;
    }

    @Transactional
    public RsData<AuthAndMakeTokenResponse> authAndMakeToken(String username, String password) {
        Member member = this.memberRepository.findByUsername(username).orElseThrow(() -> new GlobalException("400-1", "해당 회원 없음"));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalException("400-1", "비밀번호를 확인하세요.");
        }
        String refreshToken = member.getRefreshToken();

        String accessToken = jwtProvider.generateToken(member, 60 * 60 * 5);
        return RsData.of(
                "200-1",
                "로그인 성공",
                new AuthAndMakeTokenResponse(member, accessToken, refreshToken)
        );
    }
}
