package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.domain.SocialType;
import com.backend.NawaBari.jwt.JwtService;
import com.backend.NawaBari.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        Member member = memberRepository.findOne(memberId);

        String accessToken = jwtService.createAccessToken(member.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        memberRepository.saveRefreshToken(member.getEmail(), refreshToken);

        return AuthTokens.of(memberId, accessToken, refreshToken);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {

        String email = oAuthInfoResponse.getEmail();
        if (email == null) {
            email = UUID.randomUUID() + "@nawabari.com";
        }

        Member member = Member.builder()
                .kakao_id(oAuthInfoResponse.getKakao_id())
                .age(oAuthInfoResponse.getAgeRange())
                .gender(oAuthInfoResponse.getGender())
                .socialType(SocialType.KAKAO)
                .email(email)
                .profile_nickname(oAuthInfoResponse.getNickname())
                .profile_image(oAuthInfoResponse.getProfileImage())
                .role(Role.GUEST)
                .build();

        memberRepository.save(member);
        return member.getId();
    }
}