package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    private final JwtService jwtService;

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