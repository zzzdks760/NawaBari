package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("id")
    private Long kakao_id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
        private String gender;
        private String age_range;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
        private String thumbnail_image_url;
    }

    @Override
    public String getKakao_id() {
        return String.valueOf(kakao_id);
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getProfileImage() {
        return kakaoAccount.profile.thumbnail_image_url;
    }

    @Override
    public String getGender() {
        return kakaoAccount.gender;
    }

    @Override
    public String getAgeRange() {
        return kakaoAccount.age_range;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}