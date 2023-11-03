package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchDTO {
    private String name;
    private String address_name;
    private Long zoneId;


    public static RestaurantSearchDTO convertToDTO(Restaurant restaurant) {
        RestaurantSearchDTO restaurantSearchDTO = new RestaurantSearchDTO();
        restaurantSearchDTO.setName(restaurant.getName());
        restaurantSearchDTO.setAddress_name(restaurant.getAddress_name());
        restaurantSearchDTO.setZoneId(restaurant.getZone().getId());
        return restaurantSearchDTO;
    }
}
