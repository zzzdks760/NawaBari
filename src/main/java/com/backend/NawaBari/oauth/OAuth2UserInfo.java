package com.backend.NawaBari.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getKakao_id();

    public abstract String getProfile_nickname();

    public abstract String getProfile_image();

    public abstract String getAgeRange();

    public abstract String getGender();

    public abstract String getEmail();

}
