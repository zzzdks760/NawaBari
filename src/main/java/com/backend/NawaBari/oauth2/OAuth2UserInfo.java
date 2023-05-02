package com.backend.NawaBari.oauth2;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attribute;

    public OAuth2UserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getProfile_image();

    public abstract String getGender();

    public abstract String getAgeRange();
}
