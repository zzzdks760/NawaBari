package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.review.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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
    private String age;
    private List<MemberZone> memberZones = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Heart> hearts = new ArrayList<>();

}
