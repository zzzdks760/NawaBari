package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.SocialType;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    SocialType socialType();
    MultiValueMap<String, String> makeBody();
}