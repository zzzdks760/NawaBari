package com.backend.NawaBari.converter;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.dto.MemberDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemberConverter {

    public Member toEntity(MemberDTO memberDTO) {
        return Member.builder()
                .kakao_id(memberDTO.getKakao_id())
                .profile_nickname(memberDTO.getProfile_nickname())
                .profile_image(memberDTO.getProfile_image())
                .gender(memberDTO.getGender())
                .age(memberDTO.getAge_range())
                .build();
    }

    public List<Member> toEntityList(List<MemberDTO> memberDTOList) {
        return memberDTOList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public MemberDTO toDTO(Map<String, String> userInfo) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setKakao_id(userInfo.get("kakao_id"));
        memberDTO.setProfile_nickname(userInfo.get("profile_nickname"));
        memberDTO.setProfile_image(userInfo.get("profile_image"));
        memberDTO.setAge_range(userInfo.get("age_range"));
        memberDTO.setGender(userInfo.get("gender"));
        return memberDTO;
    }

    public List<MemberDTO> toDTOList(List<Map<String, String>> userInfoList) {
        return userInfoList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
