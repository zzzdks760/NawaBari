package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Long restaurantId;
    private String name;
    private String main_photo_url;
    private int reviewCount;
    private double avgRating;
    private String topReviewTitle;
    private Long zoneId;


    public static RestaurantDTO convertToDTO(Restaurant restaurant) {

        RestaurantDTO restaurantDTO = new RestaurantDTO();

        restaurantDTO.setRestaurantId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setMain_photo_url(restaurant.getMain_photo_url());
        restaurantDTO.setAvgRating(restaurant.getAvgRating());
        restaurantDTO.setReviewCount(restaurant.getReviewCount());
        restaurantDTO.setZoneId(restaurant.getZone().getId());

        return restaurantDTO;
    }
}
