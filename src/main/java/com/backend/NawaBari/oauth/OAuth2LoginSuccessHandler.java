package com.backend.NawaBari.oauth;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.jwt.JwtService;
import com.backend.NawaBari.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
            String refreshToken = jwtService.createRefreshToken();

            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
            response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

            memberRepository.saveRefreshToken(oAuth2User.getEmail(), refreshToken);

            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

            // User의 Role이 GUEST일 경우 구역설정 페이지로 이동
            if(oAuth2User.getRole() == Role.GUEST) {


                response.sendRedirect("/api/memberZone"); // 프론트의 구역 정보 입력 폼으로 리다이렉트

            } else {
                    response.sendRedirect("/api/main");
                }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
