package com.backend.NawaBari.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSignUpDTO {

    private String email;
    private String password;
    private String profile_nickname;
    private String profile_image;
    private String age;
    private String gender;
}
