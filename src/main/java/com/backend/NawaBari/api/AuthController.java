package com.backend.NawaBari.api;

import com.backend.NawaBari.jwt.JwtAuthenticationProcessingFilter;
import com.backend.NawaBari.jwt.JwtBlacklistService;
import com.backend.NawaBari.jwt.JwtService;
import com.backend.NawaBari.oauth2.AuthTokens;
import com.backend.NawaBari.oauth2.KakaoLoginParams;
import com.backend.NawaBari.oauth2.OAuthLoginService;
import com.backend.NawaBari.service.KakaoLogoutService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    private final JwtService jwtService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final KakaoLogoutService kakaoLogoutService;

    /**
     * 인가코드 받아 로그인처리후
     * 헤더: 액세스토큰, 리프레시토큰, 회원아이디 반환
     * 바디: 액세스토큰 유효기간(초), grantType 반환
     */
    @GetMapping("/login/oauth2/code/kakao")
    public String loginKakao(@RequestParam("code") String code) {
        KakaoLoginParams params = new KakaoLoginParams(code);

        AuthTokens authTokens = oAuthLoginService.login(params);

        String accessToken = authTokens.getAccessToken();
        String refreshToken = authTokens.getRefreshToken();
        Long memberId = authTokens.getMember_id();

       return  "redirect:callback-scheme://?" + "access-token=" + accessToken + "&refresh-token=" + refreshToken + "&member-id=" + memberId
                + "<!DOCTYPE html>" +
                "<html lang='en'>"+
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Redirect to App Test</title>" +
                "</head>" +
                "<body>" +
                "<button onclick='redirectToApp()'>앱으로 돌아가기</button>" +
                "<script>" +
                "function redirectToApp() {" +
                "window.location.href = 'callback-scheme://?access-token=....';" +
                "}" +
                "</script>" +
                "</body>" +
                "</html>";
    }

    /**
     * 리프레시토큰을 받아 재로그인
     */
    @PostMapping("/api/reLogin")
    public ResponseEntity<?> reLogin(@RequestHeader("Authorization-refresh") String refreshTokenHeader, HttpServletResponse response) {
        // 클라이언트가 보낸 refreshTokenHeader 값은 "Bearer " 뒤에 실제 refreshToken이 붙어 있으므로,
        // "Bearer "를 제거하여 실제 refreshToken만 추출한다.
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");

        if (jwtBlacklistService.isTokenBlacklist(refreshToken)) {
            String errorMessage = "이미 사용된 토큰입니다.";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage); //403응답
        } else if (!jwtService.isTokenValid(refreshToken)) {
            String errorMessage = "유효하지 않은 토큰입니다";
            jwtBlacklistService.blacklistToken(refreshToken);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage); //403응답
        } else {
            jwtAuthenticationProcessingFilter.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);

            return ResponseEntity.ok().body(new AddReInfoResponse("Bearer", jwtService.getAccessTokenExpirationPeriod()));
        }
    }

    /**
     * 로그아웃 (리프레시토큰 블랙리스트에 추가)
     */
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout (@RequestHeader("member_id") Long memberId) {
        kakaoLogoutService.logout(memberId);
        return ResponseEntity.ok().build();
    }

    @Data
    static class AddInfoResponse {
        private String grantType;
        private Long expiresIn;
        private String redirectUrl;

        public AddInfoResponse(String grantType, Long expiresIn, String redirectUrl) {
            this.grantType = grantType;
            this.expiresIn = expiresIn;
            this.redirectUrl = redirectUrl;
        }
    }

    @Data
    static class AddReInfoResponse {
        private String grantType;
        private Long expiresIn;

        public AddReInfoResponse(String grantType, Long expiresIn) {
            this.grantType = grantType;
            this.expiresIn = expiresIn;
        }
    }
}