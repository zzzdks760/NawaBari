package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.service.RestaurantService;
import com.backend.NawaBari.service.ReviewService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

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

    //통합 검색
/*    @GetMapping("api/v1/restaurants/search")
    public List<RestaurantDTO> keywordSearch(@RequestParam String keyword) {
        List<Restaurant> restaurants = restaurantService.searchByNameAndAddress(keyword);

        List<RestaurantDTO> restaurantDTOS = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return restaurantDTOS;
    }*/

    @GetMapping("api/v1/restaurants/search")
    public Slice<RestaurantDTO> keywordSearch(@RequestBody String keyword, Pageable pageable) {
        Slice<Restaurant> restaurants = restaurantService.searchByNameAndAddress(keyword, pageable);

        Slice<RestaurantDTO> restaurantDTOS = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return restaurantDTOS;

    //식당 상세조회
/*    @GetMapping("/api/v1/restaurants/{restaurantId}")
    public RestaurantDetailDTO RestaurantDetail(@PathVariable Long restaurantId) {
        Restaurant restaurantDetail = restaurantService.findOne(restaurantId);

        if (restaurantDetail == null) {
            throw new IllegalArgumentException("식당을 찾을수 없습니다." + restaurantId);
        }

        List<Review> reviews = reviewService.findReviewList(restaurantId);

        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO(
                restaurantDetail.getId(),
                restaurantDetail.getName(),
                restaurantDetail.getRestaurant_img(),
                restaurantDetail.getOpeningTime(),
                restaurantDetail.getClosingTime(),
                restaurantDetail.getAddress_name(),
                restaurantDetail.getLat(),
                restaurantDetail.getLng(),
                restaurantDetail.getTel(),
                restaurantDetail.getReviewCount(),
                restaurantDetail.getAvgRating(),
                reviews
        );

        return restaurantDetailDTO;
    }*/

//===============================================================================================================//


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

    static class RestaurantDetailDTO {
        private Long id;
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

        public RestaurantDetailDTO(Long id, String name, String restaurant_img, String openingTime, String closingTime, String address_name, Double lat, Double lng, String tel, int reviewCount, Double avgRating, List<Review> reviews) {
            this.id = id;
            this.name = name;
            this.restaurant_img = restaurant_img;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
            this.address_name = address_name;
            this.lat = lat;
            this.lng = lng;
            this.tel = tel;
            this.reviewCount = reviewCount;
            this.avgRating = avgRating;
            this.reviews = reviews;
        }
    }
}

