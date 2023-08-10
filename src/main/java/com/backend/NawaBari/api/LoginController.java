package com.backend.NawaBari.api;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.jwt.JwtAuthenticationProcessingFilter;
import com.backend.NawaBari.jwt.JwtBlacklistService;
import com.backend.NawaBari.jwt.JwtService;
import com.backend.NawaBari.oauth.CustomOAuth2User;
import com.backend.NawaBari.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final JwtBlacklistService jwtBlacklistService;


/*    *//**
     * 인가코드가져오기
     *//*
    @GetMapping("/Inga")
    public void login(@RequestParam String code) {
        System.out.println("code = " + code);
    }


    *//**
     * 인가코드 받아서 토큰, 아이디 반환
     *//*
    @GetMapping("/api/login")
    public ResponseEntity<TokenInfoResponse> getTokenInfo(@RequestParam("code") String authorizationCode) {

        //액세스토큰 생성
        String accessToken = jwtService.createAccessToken();
        System.out.println("액세스토큰 생성 = " + accessToken);
        //액세스토큰으로부터 이메일추출
        Optional<String> extractEmail = jwtService.extractEmail(accessToken);
        System.out.println("추출된 email = " + extractEmail);

        if (extractEmail.isPresent()) {
            String email = extractEmail.get();
            //리프레시토큰 생성
            String refreshToken = jwtService.createRefreshToken();
            //memberRepository.saveRefreshToken(email, refreshToken);

            Optional<Member> optionalMember = memberRepository.findByEmail(email);
            Long id = optionalMember.map(Member::getId).orElse(null);

            //액세스토큰, 리프레시토큰, 회원아이디를 TokenInfoResponse에 담아 반환
            TokenInfoResponse response = new TokenInfoResponse(accessToken, refreshToken, id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }*/

    /**
     * 재 로그인
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<?> reLogin(@RequestHeader("Authorization") String refreshTokenHeader, HttpServletResponse response) {
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
    }

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
