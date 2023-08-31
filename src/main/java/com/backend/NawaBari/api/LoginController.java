package com.backend.NawaBari.api;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
@RestController
public class LoginController {

/*    private final KakaoOAuth2 kakaoOAuth;
    private final OAuthService oAuthService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;


    *//**
     * 카카오 로그인페이지 반환
     *//*
    @GetMapping("/kakao")
    public void getKakaoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoOAuth.responseUrl());
    }*/

}
