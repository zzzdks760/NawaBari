package com.backend.NawaBari.oauth2;

import com.backend.NawaBari.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
@Getter
public class DefaultOAuth2User extends org.springframework.security.oauth2.core.user.DefaultOAuth2User {

    private String kakao_id;
    private Role role;

    public DefaultOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String kakao_id, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.kakao_id = kakao_id;
        this.role = role;
    }
}
