package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.SocialType;

public interface OAuthApiClient {
    SocialType socialType();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}