package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder

    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
            return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public Member toEntity(OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .kakao_id(oauth2UserInfo.getId())
                .profile_nickname(oauth2UserInfo.getNickname())
                .profile_image(oauth2UserInfo.getProfile_image())
                .age(oauth2UserInfo.getAgeRange())
                .gender(oauth2UserInfo.getGender())
                .role(Role.GUEST)
                .build();
    }

    public OAuth2UserInfo getOauth2UserInfo() {
            return oAuth2UserInfo;
    }
}
