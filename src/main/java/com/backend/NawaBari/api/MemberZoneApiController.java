package com.backend.NawaBari.api;


import com.backend.NawaBari.exception.MaximumZoneLimitException;
import com.backend.NawaBari.exception.ZoneAlreadySetException;
import com.backend.NawaBari.service.CurrentLocationService;
import com.backend.NawaBari.service.MemberZoneService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberZoneApiController {

    private final MemberZoneService memberZoneService;
    private final CurrentLocationService currentLocationService;

    //구역설정
    @PostMapping("/api/v1/members/memberZone")
    public ResponseEntity<List<MemberZoneResponseDTO>> setMemberZone(@RequestBody MemberZoneRequestDTO requestDTO) {
        try {
            Long memberId = requestDTO.getMemberId();
            float lat = requestDTO.getLat();
            float lng = requestDTO.getLng();
            CurrentLocationService.LocationInfo guAndDong = currentLocationService.getGuAndDong(lat, lng);
            if (guAndDong == null) {
                return ResponseEntity.notFound().build();
            }
            String guName = guAndDong.getGuName();
            String dongName = guAndDong.getDongName();

            List<Long> memberZones = memberZoneService.setMemberZone(memberId, guName, dongName);

            return ResponseEntity.ok(memberZones.stream()
                    .map(MemberZoneResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (MaximumZoneLimitException | ZoneAlreadySetException e) {
            List<MemberZoneResponseDTO> errors = new ArrayList<>();
            errors.add(new MemberZoneResponseDTO(e.getMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
    }




    //===============================================================================================================//

    @Data
    static class MemberZoneResponseDTO {
        private Long memberZoneId;
        private String eMessage;


        public MemberZoneResponseDTO(Long memberZoneId) {
            this.memberZoneId = memberZoneId;
        }

        public MemberZoneResponseDTO(String eMessage) {
            this.eMessage = eMessage;
        }
    }

    @Data
    static class MemberZoneRequestDTO {
        private Long memberId;
        private float lat;
        private float lng;
    }

}
