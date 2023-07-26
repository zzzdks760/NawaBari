package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.service.CurrentLocationService;
import com.backend.NawaBari.service.RestaurantService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CurrentLocationApiController {
    private final CurrentLocationService currentLocationService;
    private final RestaurantService restaurantService;

    /**
     * 현위치를 받아와 동 이름이 일치하는 식당들 조회
     */
    @PostMapping("/api/v1/location/restaurants")
    public Slice<RestaurantApiController.RestaurantDTO> getCurrentLocation(@RequestBody LocationDTO locationDTO,
                                                                           @PageableDefault Pageable pageable) {
        float current_lat = locationDTO.getCurrent_lat();
        float current_lng = locationDTO.getCurrent_lng();
        String dongName = currentLocationService.getCurrentLocation(current_lat, current_lng);

        Slice<Restaurant> restaurants = restaurantService.searchByNameAndAddress(dongName, pageable);

        List<RestaurantApiController.RestaurantDTO> restaurantDTOS = restaurants.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }


    @Data
    static class LocationDTO {
        private float current_lat;
        private float current_lng;
    }

    private RestaurantApiController.RestaurantDTO convertToDTO(Restaurant restaurant) {

        RestaurantApiController.RestaurantDTO restaurantDTO = new RestaurantApiController.RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getRestaurant_img(),
                restaurant.getAddress_name(),
                restaurant.getLat(),
                restaurant.getLng(),
                restaurant.getReviewCount(),
                restaurant.getAvgRating()
        );
        return restaurantDTO;
    }

}
