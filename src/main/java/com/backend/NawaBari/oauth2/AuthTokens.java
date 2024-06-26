package com.backend.NawaBari.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {
    private Long member_id;
    private String accessToken;
    private String refreshToken;

    public static AuthTokens of(Long member_id, String accessToken, String refreshToken) {
        return new AuthTokens(member_id, accessToken, refreshToken);
    }
}