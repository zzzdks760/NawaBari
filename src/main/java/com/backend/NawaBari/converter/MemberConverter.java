package com.backend.NawaBari.converter;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.dto.MemberDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberConverter {

    public Member toEntity(MemberDTO memberDTO) {
        return Member.builder()
                .kakao_id(memberDTO.getKakao_id())
                .profile_nickname(memberDTO.getProfile_nickname())
                .profile_image(memberDTO.getProfile_image())
                .gender(memberDTO.getGender())
                .age(memberDTO.getAge())
                .build();
    }

    public List<Member> toEntityList(List<MemberDTO> memberDTOList) {
        return memberDTOList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .kakao_id(member.getKakao_id())
                .profile_nickname(member.getProfile_nickname())
                .profile_image(member.getProfile_image())
                .gender(member.getGender())
                .age(member.getAge())
                .build();
    }

    public List<MemberDTO> toDTOList(List<Member> memberList) {
        return memberList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
