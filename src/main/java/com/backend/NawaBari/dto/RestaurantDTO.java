package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Restaurant;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private String name;

    private String address_name;

    private Double lat;

    private Double lng;

    public static List<Restaurant> toEntityList(List<RestaurantDTO> restaurantDTOList) {
        return restaurantDTOList.stream()
                .map(restaurantDTO -> Restaurant.builder()
                        .name(restaurantDTO.getName())
                        .address_name(restaurantDTO.getAddress_name())
                        .lat(restaurantDTO.getLat())
                        .lng(restaurantDTO.getLng())
                        .build())
                .collect(Collectors.toList());
    }
}
