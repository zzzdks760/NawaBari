package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.exception.MaximumZoneLimitException;
import com.backend.NawaBari.exception.ZoneAlreadySetException;
import com.backend.NawaBari.service.CurrentLocationService;
import com.backend.NawaBari.service.MemberZoneService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberZoneApiController {

    private final MemberZoneService memberZoneService;
    private final CurrentLocationService currentLocationService;

    //구역설정
    @PostMapping("/api/v1/members/memberZone")
    public MemberZoneResponseDTO setMemberZone(@RequestBody MemberZoneRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        float lat = requestDTO.getLat();
        float lng = requestDTO.getLng();
        CurrentLocationService.LocationInfo guAndDong = currentLocationService.getGuAndDong(lat, lng);
        String guName = guAndDong.getGuName();
        String dongName = guAndDong.getDongName();
        System.out.println("guName = " + guName);
        System.out.println("dongName = " + dongName);

        Long memberZoneId = memberZoneService.setMemberZone(memberId, guName, dongName);
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
        private float lat;
        private float lng;
    }

}
