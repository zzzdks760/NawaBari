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
public class MainPageDTO {
    private Long restaurantId;
    private String name;
    private String main_photo_url;
    private String guName;
    private String dongName;
    private int reviewCount;
    private double avgRating;
    private String topReviewTitle;



    public static MainPageDTO convertToDTO(Restaurant restaurant) {

        MainPageDTO mainPageDTO = new MainPageDTO();

        mainPageDTO.setRestaurantId(restaurant.getId());
        mainPageDTO.setName(restaurant.getName());
        mainPageDTO.setMain_photo_url(restaurant.getMain_photo_url());
        mainPageDTO.setReviewCount(restaurant.getReviewCount());
        mainPageDTO.setAvgRating(restaurant.getAvgRating());
        return mainPageDTO;
    }
}
