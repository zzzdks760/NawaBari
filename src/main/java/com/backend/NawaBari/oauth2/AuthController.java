package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.jwt.JwtAuthenticationProcessingFilter;
import com.backend.NawaBari.jwt.JwtBlacklistService;
import com.backend.NawaBari.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    private final JwtService jwtService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;

    /**
     * 인가코드 받아 로그인처리후
     * 헤더: 액세스토큰, 리프레시토큰, 회원아이디 반환
     * 바디: 액세스토큰 유효기간(초), grantType 반환
     */
    @PostMapping("/api/auth/kakao")
    public ResponseEntity<AddInfoResponse> loginKakao(@RequestBody KakaoLoginParams params, HttpServletResponse response) {
        AuthTokens authTokens = oAuthLoginService.login(params);

        String accessToken = authTokens.getAccessToken();
        String refreshToken = authTokens.getRefreshToken();
        Long member_id = authTokens.getMember_id();

        response.setHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.setHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
        response.setHeader("member_id", member_id.toString());

        return ResponseEntity.ok(new AddInfoResponse("Bearer", jwtService.getAccessTokenExpirationPeriod()));
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

            return ResponseEntity.ok().body(new AddInfoResponse("Bearer", jwtService.getAccessTokenExpirationPeriod()));
        }
    }

    @Data
    static class AddInfoResponse {
        private String grantType;
        private Long expiresIn;

        public AddInfoResponse(String grantType, Long expiresIn) {
            this.grantType = grantType;
            this.expiresIn = expiresIn;
        }
    }
}