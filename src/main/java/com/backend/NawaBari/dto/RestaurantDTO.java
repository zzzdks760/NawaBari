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
    private String address_name;
    private int reviewCount;
    private double avgRating;
    private String topReviewTitle;


    public static RestaurantDTO convertToDTO(Restaurant restaurant) {

        RestaurantDTO restaurantDTO = new RestaurantDTO();

        restaurantDTO.setRestaurantId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setMain_photo_url(restaurant.getMain_photo_url());
        restaurantDTO.setAddress_name(restaurant.getAddress_name());
        restaurantDTO.setAvgRating(restaurant.getAvgRating());
        restaurantDTO.setReviewCount(restaurant.getReviewCount());

        return restaurantDTO;
    }
}
