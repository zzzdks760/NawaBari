package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {

    private String profile_nickname;
    private String profile_image;
    private Role role;
    private List<Long> zoneIds;
    private List<String> dongNames;
}
