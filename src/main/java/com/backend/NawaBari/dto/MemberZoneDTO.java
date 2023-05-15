package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberZoneDTO {

    private Long id;

    private Member member;

    private Zone zone;
}
