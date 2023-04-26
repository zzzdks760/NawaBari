package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.review.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
