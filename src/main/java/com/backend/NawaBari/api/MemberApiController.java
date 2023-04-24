package com.backend.NawaBari.api;

import com.backend.NawaBari.converter.TokenConverter;
import com.backend.NawaBari.dto.TokenDTO;
import com.backend.NawaBari.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;
    private final TokenConverter tokenConverter;

    /**
     * 사용자 토큰 받기
     */
    @PostMapping("/access_token")
    public void getToken(@RequestParam TokenDTO tokenDTO) {
        HashMap<String, Object> userInfo = memberService.getUserInfo(tokenDTO.getAccess_token());

    }

    /**
     * 카카오 로그인
     */
    @GetMapping("/login")
    public void login(@RequestParam String code) {
        String access_Token = memberService.getKakaoAccessToken(code);
        System.out.println("code = " + code);
        HashMap<String, Object> userInfo = memberService.getUserInfo(access_Token);
        System.out.println("userInfo = " + userInfo);
    }

    /**
     * 카카오 로그아웃
     */
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        memberService.kakaoLogout((String)session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
    }
}
