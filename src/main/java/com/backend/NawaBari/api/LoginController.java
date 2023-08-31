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
    }


    *//**
     * 인가코드 받아오기
     *//*
    @GetMapping("/login/kakao")
    public TokenUserDTO kakaoLogin (@RequestParam(name = "code") String code) throws IOException {
        log.info("카카오 API 서버 code : " + code);
        return oAuthService.kakaoLogin(code);
    }*/




    /**
     * 로그인버튼 클릭시
     */
/*    @GetMapping("/auth/kakao/login")
    public ResponseEntity<Void> startKakaoLogin(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestResolver.resolve(request);
        if (authorizationRequest != null) {
            String authorizationUri = authorizationRequest.getAuthorizationRequestUri();
            response.setHeader("Location", authorizationUri);
            response.setStatus(HttpServletResponse.SC_FOUND);
            System.out.println("Location = " + response.getHeader("Location"));

            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        return ResponseEntity.badRequest().build();
    }*/


    /**
     * 재 로그인
     */
/*    @PostMapping("/refreshToken")
    public ResponseEntity<?> reLogin(@RequestHeader("Authorization-refresh") String refreshTokenHeader, HttpServletResponse response) {
        // 클라이언트가 보낸 refreshTokenHeader 값은 "Bearer " 뒤에 실제 refreshToken이 붙어 있으므로,
        // "Bearer "를 제거하여 실제 refreshToken만 추출한다.
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");
        System.out.println("Received Refresh Token: " + refreshToken);

        if (jwtBlacklistService.isTokenBlacklist(refreshToken)) {
            String errorMessage = "만료된 토큰입니다.";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage); //403응답
        }

        jwtBlacklistService.blacklistToken(refreshToken);

        // 추출한 refreshToken을 JwtAuthenticationProcessingFilter의 메서드로 전달하여 처리한다.
        // 이때, response 객체를 직접 넘겨줘서 필터 내부에서 sendAccessAndRefreshToken() 메서드를 사용할 수 있도록 한다.
        jwtAuthenticationProcessingFilter.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
        System.out.println("새로운 리프레시토큰 = " + refreshToken);

        return ResponseEntity.ok().build();
    }*/

    @Data
    static class TokenInfoResponse {
        private String accessToken;
        private String refreshToken;
        private Long id;

        public TokenInfoResponse(String accessToken, String refreshToken, Long id) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.id = id;
        }
    }
}
