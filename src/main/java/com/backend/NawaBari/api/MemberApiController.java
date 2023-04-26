package com.backend.NawaBari.api;

import com.backend.NawaBari.converter.TokenConverter;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.dto.MemberDTO;
import com.backend.NawaBari.dto.TokenDTO;
import com.backend.NawaBari.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final TokenConverter tokenConverter;


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
     * 토큰만료시 카카오 로그인
     */
    @PostMapping("/refreshLogin")
    public void refreshLogin(@RequestParam String refresh_Token) {
        String accessToken = memberService.getNewAccessToken(refresh_Token);
        memberService.getUserInfo(accessToken);
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