package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.service.RestaurantService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;

    //식당 검색
    @GetMapping("/api/main/search")
    public List<RestaurantDTO> RestaurantSearch(@RequestParam(required = false) String name, @RequestParam(required = false) String addressName) {
        List<Restaurant> restaurants;
        if (name != null) {
            restaurants = restaurantService.findByRestaurantName(name);
        } else if (addressName != null) {
            restaurants = restaurantService.findByAddressName(addressName);
        } else {
            // Invalid request, name or address should be provided
            return Collections.emptyList();
        }

        List<RestaurantDTO> restaurantDTOs = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return restaurantDTOs;
    }


    @Data
    static class RestaurantDTO {
        private Long restaurantId;
        private String name;
        private String restaurant_img;
        private String address_name;
        private Double lat;
        private Double lng;
        private int reviewCount;
        private Double avgRating;

        public RestaurantDTO(Long restaurantId, String name, String restaurant_img, String address_name, Double lat, Double lng, int reviewCount, Double avgRating) {
            this.restaurantId = restaurantId;
            this.name = name;
            this.restaurant_img = restaurant_img;
            this.address_name = address_name;
            this.lat = lat;
            this.lng = lng;
            this.reviewCount = reviewCount;
            this.avgRating = avgRating;
        }
    }
    private RestaurantDTO convertToDTO(Restaurant restaurant) {

        RestaurantDTO restaurantDTO = new RestaurantDTO(
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

