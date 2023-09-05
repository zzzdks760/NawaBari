package com.backend.NawaBari.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MemberDTO {
    private Long id;
    private String kakao_id;
    private String profile_nickname;
    private String profile_image;
    private String gender;
    private String age_range;

    public MemberDTO(Map<String, Object> userInfo) {
        this.kakao_id = (String) userInfo.get("kakao_id");
        this.profile_nickname = (String) userInfo.get("profile_nickname");
        this.profile_image = (String) userInfo.get("profile_image");
        this.age_range = (String) userInfo.get("age_range");
        this.gender = (String) userInfo.get("gender");
    }

}
