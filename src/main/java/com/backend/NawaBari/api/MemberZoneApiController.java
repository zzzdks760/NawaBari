package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.MemberZoneDTO;
import com.backend.NawaBari.service.MemberZoneService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberZoneApiController {

    private final MemberZoneService memberZoneService;

    //구역설정
    @PostMapping("/api/memberZone")
    public MemberZoneResponseDTO setMemberZone(@RequestBody @Validated MemberZoneRequestDTO request) {
        Long memberZoneId = memberZoneService.setMemberZone(request.getMemberId(), request.getZoneId());

        return new MemberZoneResponseDTO(memberZoneId);
    }


    //===============================================================================================================//

    @Data
    static class MemberZoneResponseDTO {
        private Long memberZoneId;

        public MemberZoneResponseDTO(Long memberZoneId) {
            this.memberZoneId = memberZoneId;
        }
    }

    @Data
    static class MemberZoneRequestDTO {
        private Long memberId;
        private Long zoneId;
    }
}
