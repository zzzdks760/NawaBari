package com.backend.NawaBari.api;
import com.backend.NawaBari.jwt.JwtAuthenticationProcessingFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader, HttpServletResponse response) {
        // 클라이언트가 보낸 refreshTokenHeader 값은 "Bearer " 뒤에 실제 refreshToken이 붙어 있으므로,
        // "Bearer "를 제거하여 실제 refreshToken만 추출한다.
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");
        System.out.println("Received Refresh Token: " + refreshToken);

        // 추출한 refreshToken을 JwtAuthenticationProcessingFilter의 메서드로 전달하여 처리한다.
        // 이때, response 객체를 직접 넘겨줘서 필터 내부에서 sendAccessAndRefreshToken() 메서드를 사용할 수 있도록 한다.
        jwtAuthenticationProcessingFilter.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);

        return ResponseEntity.ok().build();
    }
}
