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

            // User의 Role이 GUEST일 경우 구역설정 페이지로 이동
            if(oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();

                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

                memberRepository.saveRefreshToken(oAuth2User.getEmail(), refreshToken);

                jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

                response.sendRedirect("/login"); // 프론트의 구역 정보 입력 폼으로 리다이렉트하고 구역을 설정했다면 MEMBER로 변경

                // Role이 MEMBER일 경우 메인페이지로 이동
            } else {
                jwtService.updateRefreshToken(oAuth2User.getEmail(), jwtService.createRefreshToken());
                response.sendRedirect("/main");
            }
        } catch (Exception e) {
            throw e;
        }

    }

/*    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }*/
}
