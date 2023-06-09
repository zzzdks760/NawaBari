package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address_name;
    private Double lat;
    private Double lng;
    private String tel;

    public static List<Restaurant> toEntityList(List<RestaurantDTO> restaurantDTOList) {
        return restaurantDTOList.stream()
                .map(restaurantDTO -> Restaurant.builder()
                        .name(restaurantDTO.getName())
                        .address_name(restaurantDTO.getAddress_name())
                        .lng(restaurantDTO.getLng())
                        .lat(restaurantDTO.getLat())
                        .tel(restaurantDTO.getTel())
                        .build())
                .collect(Collectors.toList());
    }
}
