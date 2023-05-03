package com.backend.NawaBari.oauth2;


import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attribute) {
        super(attribute);
    }

    @Override
    public String getId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attribute.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        String profile_nickname = (String) profile.get("nickname");


        return (String) profile.get("nickname");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getProfile_image() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("profile_image_url");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getAgeRange() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        return (String) account.get("age_range");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getGender() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        return (String) account.get("gender");
    }
}
