package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Zone;
import com.google.gson.Gson;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {

    private Long id;
    private Long cig_cd;
    private String gu;
    private String dong;
    private Double lat;
    private Double lng;


    public static List<Zone> toEntityList(List<ZoneDTO> zoneDTOList) {
        return zoneDTOList.stream()
                .map(zoneDTO -> Zone.builder()
                        .cig_cd(zoneDTO.getCig_cd())
                        .gu(zoneDTO.getGu())
                        .dong(zoneDTO.getDong())
                        .lat(zoneDTO.getLat())
                        .lng(zoneDTO.getLng())
                        .build())
                .collect(Collectors.toList());
    }

    public static ZoneDTO toZoneDTO(Zone findZone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(findZone.getId());
        zoneDTO.setCig_cd(findZone.getCig_cd());
        zoneDTO.setGu(findZone.getGu());
        zoneDTO.setDong(findZone.getDong());
        zoneDTO.setLat(findZone.getLat());
        zoneDTO.setLng(findZone.getLng());

        return zoneDTO;
    }
}
