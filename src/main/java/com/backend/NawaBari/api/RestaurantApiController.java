package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.service.RestaurantService;
import com.backend.NawaBari.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;

    //식당 검색
/*    @GetMapping("/api/v1/main/keyword-search")
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
    }*/

    //통합검색
    @GetMapping("/api/v1/restaurants/search")
    public Slice<RestaurantDTO> keywordSearch(@RequestParam("keyword") String keyword, @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Slice<Restaurant> restaurants = restaurantService.searchByNameAndAddress(keyword, pageable);

        List<RestaurantDTO> restaurantDTOS = restaurants.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }


    //식당 상세조회
    @GetMapping("/api/v1/restaurants/{restaurantId}")
    public RestaurantDetailDTO RestaurantDetail(@PathVariable Long restaurantId) {
        Restaurant restaurantDetail = restaurantService.detailRestaurant(restaurantId);

        if (restaurantDetail == null) {
            throw new IllegalArgumentException("식당을 찾을수 없습니다." + restaurantId);
        }

        return new RestaurantDetailDTO(restaurantDetail.getName(), restaurantDetail.getRestaurant_img(), restaurantDetail.getOpeningTime(), restaurantDetail.getClosingTime(),
                restaurantDetail.getAddress_name(), restaurantDetail.getLat(), restaurantDetail.getLng(), restaurantDetail.getTel(), restaurantDetail.getReviewCount(),
                restaurantDetail.getAvgRating(), restaurantDetail.getReviews());
    }


//===============================================================================================================//


    @Data
    @AllArgsConstructor
    static class RestaurantDTO {
        private Long restaurantId;
        private String name;
        private String restaurant_img;
        private String address_name;
        private Double lat;
        private Double lng;
        private int reviewCount;
        private Double avgRating;
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


    @Data
    @AllArgsConstructor
    static class RestaurantDetailDTO {
        private String name;
        private String restaurant_img;
        private String openingTime;
        private String closingTime;
        private String address_name;
        private Double lat;
        private Double lng;
        private String tel;
        private int reviewCount;
        private Double avgRating;
        private List<Review> reviews;
    }
}

