package com.example.JWT.domain.member.controller;


import com.example.JWT.domain.member.dto.MemberDto;
import com.example.JWT.domain.member.service.MemberService;
import com.example.JWT.global.jwt.JwtProvider;
import com.example.JWT.global.rq.Rq;
import com.example.JWT.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;
    private final JwtProvider jwtProvider;

    @Getter
    public static class LoginRequestBody {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private MemberDto memberDto;

    }

    @PostMapping("/login")
    public RsData login(@Valid @RequestBody LoginRequestBody loginRequestBody) {
        RsData<MemberService.AuthAndMakeTokenResponse> rsData = memberService.authAndMakeToken(loginRequestBody.getUsername(), loginRequestBody.getPassword());

        // 토큰을 쿠키에 등록
        rq.setCrossDomainCookie("accessToken", rsData.getData().getAccessToken());

        System.out.println("토큰:" + rsData.getData().getAccessToken());
        return RsData.of(
                rsData.getResultCode(),
                rsData.getMsg(),
                new LoginResponse(new MemberDto(rsData.getData().getMember())));
    }

    @GetMapping("/loginCheck")
    public boolean isLoggedIn() {
        jwtProvider.verify()
    }

    @PostMapping("/logout")
    public RsData logout() {
        rq.setCrossDomainCookie("accessToken", null);
        return RsData.of(
                "200",
                "로그아웃");
    }
}
