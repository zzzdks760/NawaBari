package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    //액세스토큰발급
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String authUrl;

    //유저정보발급
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String apiUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject(authUrl, request, KakaoTokens.class);

        assert response != null;
        return response.getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String propertyKeys = "[\"kakao_account.email\", \"kakao_account.profile.nickname\", \"kakao_account.profile.thumbnail_image_url\", " +
                "\"kakao_account.gender\", \"kakao_account.age_range\", \"id\"]";

        body.add("property_keys", propertyKeys);


        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(apiUrl, request, KakaoInfoResponse.class);
    }
}