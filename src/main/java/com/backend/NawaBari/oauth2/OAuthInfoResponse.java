package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.SocialType;

public interface OAuthInfoResponse {

    String getKakao_id();
    String getEmail();
    String getNickname();

    String getProfileImage();

    String getAgeRange();

    String getGender();

    SocialType getSocialType();

}