package com.backend.NawaBari.dto;


import com.backend.NawaBari.domain.Restaurant;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long restaurantId;
    private String name;
    private String address_name;
    private Double lat;
    private Double lng;
    private String tel;
    private String mainPhotoUrl;
    private int reviewCount;
    private Double avgRating;
    private Long zoneId;

    //식당데이터 삽입
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

    public static RestaurantDTO convertToDTO(Restaurant restaurant) {

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setRestaurantId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setAddress_name(restaurant.getAddress_name());
        restaurantDTO.setTel(restaurant.getTel());
        restaurantDTO.setLat(restaurant.getLat());
        restaurantDTO.setLng(restaurant.getLng());
        restaurantDTO.setReviewCount(restaurant.getReviewCount());
        restaurantDTO.setAvgRating(restaurant.getAvgRating());
        restaurantDTO.setZoneId(restaurant.getZone().getId());

        if (restaurant.getMain_photo_fileName() != null) {
            String mainPhotoFileName = restaurant.getMain_photo_fileName();

            String mainPhotoPath = "/main_images/" + mainPhotoFileName;
            restaurantDTO.setMainPhotoUrl(mainPhotoPath);
        }
        return restaurantDTO;
    }
}
