package com.example.JWT.domain.member.service;

import com.example.JWT.domain.member.entity.Member;
import com.example.JWT.domain.member.repository.MemberRepository;
import com.example.JWT.global.exception.GlobalException;
import com.example.JWT.global.jwt.JwtProvider;
import com.example.JWT.global.rsData.RsData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        memberRepository.save(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }


    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokenResponse {
        private Member member;
        private String accessToken;
    }

    @Transactional
    public RsData<AuthAndMakeTokenResponse> authAndMakeToken(String username, String password) {
        Member member = this.memberRepository.findByUsername(username).orElseThrow(() -> new GlobalException("400-1", "해당 회원 없음"));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GlobalException("400-1","비밀번호를 확인하세요.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("ID", member.getId());
        claims.put("USERNAME", member.getUsername());
        // 회원데이터, 시간

        String accessToken = jwtProvider.generateToken(claims, 60 * 60 * 5);

        return RsData.of(
                "200-1",
                "로그인 성공",
                new AuthAndMakeTokenResponse(member, accessToken)
        );
    }
}
