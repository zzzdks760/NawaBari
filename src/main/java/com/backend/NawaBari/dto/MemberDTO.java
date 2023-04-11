package com.backend.NawaBari.dto;

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

public class MemberDTO {
    private Long id;
    private String kakao_id;
    private String profile_nickname;
    private String profile_image;
    private String gender;
    private String age;
    private List<MemberZone> memberZones = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();


    public static MemberDTO toMemberDTO(Member findMember) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(findMember.getId());
        memberDTO.setKakao_id(findMember.getKakao_id());
        memberDTO.setProfile_nickname(findMember.getProfile_nickname());
        memberDTO.setProfile_image(findMember.getProfile_image());
        memberDTO.setGender(findMember.getGender());
        memberDTO.setAge(findMember.getAge());

        return memberDTO;
    }
}
