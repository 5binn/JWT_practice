package com.example.JWT.domain.member.controller;


import com.example.JWT.domain.member.dto.MemberDto;
import com.example.JWT.domain.member.entity.Member;
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
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequestBody loginRequestBody) {
        RsData<MemberService.AuthAndMakeTokenResponse> rsData = memberService.authAndMakeToken(loginRequestBody.getUsername(), loginRequestBody.getPassword());

        // 토큰을 쿠키에 등록
        rq.setCrossDomainCookie("accessToken", rsData.getData().getAccessToken());
        rq.setCrossDomainCookie("refreshToken", rsData.getData().getRefreshToken());

        System.out.println("액세스토큰:" + rsData.getData().getAccessToken());
        System.out.println("리프레시토큰:" + rsData.getData().getRefreshToken());
        return RsData.of(
                rsData.getResultCode(),
                rsData.getMsg(),
                new LoginResponse(new MemberDto(rsData.getData().getMember())));
    }
    @AllArgsConstructor
    @Getter
    public static class MeResponse {
        private final MemberDto memberDto;
    }
    @GetMapping("/me")
    public RsData<MeResponse> getMe () {
        Member member = rq.getMember();

        return RsData.of(
                "S-2",
                "성공",
                new MeResponse(new MemberDto(member))
        );
    }

//    @GetMapping("/loginCheck")
//    public boolean isLoggedIn() {
//        jwtProvider.verify()
//    }

    @PostMapping("/logout")
    public RsData logout() {
        rq.removeCrossDomainCookie("accessToken");
        rq.removeCrossDomainCookie("refreshToken");

        return RsData.of("200", "로그아웃 성공");
    }
}
