package com.backend.NawaBari.controller;

import com.backend.NawaBari.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
