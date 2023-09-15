package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.dto.ReviewDTO;
import com.backend.NawaBari.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantApiController {

    private final RestaurantService restaurantService;


    //통합검색
    @GetMapping("/api/v1/restaurants/search")
    public Slice<RestaurantDTO> keywordSearch(@RequestParam("keyword") String keyword, @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Slice<Restaurant> restaurants = restaurantService.searchByNameAndAddress(keyword, pageable);

        List<RestaurantDTO> restaurantDTOS = restaurants.getContent().stream()
                .map(RestaurantDTO::convertToDTO)
                .collect(Collectors.toList());

        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }


    //식당 상세조회
    @GetMapping("/api/v1/restaurants/restaurant")
    public RestaurantDetailDTO RestaurantDetail(@RequestParam("restaurantId") Long restaurantId) {

        Restaurant restaurant = restaurantService.detailRestaurant(restaurantId);
        List<Review> reviews = restaurant.getReviews();

        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(ReviewDTO::convertToDTO)
                .collect(Collectors.toList());


        return new RestaurantDetailDTO(restaurant.getName(), restaurant.getOpeningTime(), restaurant.getClosingTime(),
        restaurant.getAddress_name(), restaurant.getLat(), restaurant.getLng(), restaurant.getTel(), restaurant.getReviewCount(),
        restaurant.getAvgRating(), reviewDTOS, restaurant.getZone().getId());
    }


//===============================================================================================================//



    @Data
    @AllArgsConstructor
    static class RestaurantDetailDTO {
        private String name;
        private String openingTime;
        private String closingTime;
        private String address_name;
        private Double lat;
        private Double lng;
        private String tel;
        private int reviewCount;
        private Double avgRating;
        private List<ReviewDTO> reviewTop3DTOS;
        private Long zoneId;
    }
}

