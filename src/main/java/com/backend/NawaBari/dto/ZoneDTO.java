package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Zone;
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

}
