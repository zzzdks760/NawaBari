package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Member;
import lombok.*;

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

}
