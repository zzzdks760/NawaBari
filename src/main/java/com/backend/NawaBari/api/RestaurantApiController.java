package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Photo;
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

        String mainPhotoPath = null;
        if (restaurant.getMain_photo_fileName() != null) {
            mainPhotoPath = "/images/" + restaurant.getMain_photo_fileName();
        }

        return new RestaurantDetailDTO(restaurant.getName(), mainPhotoPath, restaurant.getOpeningTime(), restaurant.getClosingTime(),
        restaurant.getAddress_name(), restaurant.getLat(), restaurant.getLng(), restaurant.getTel(), restaurant.getReviewCount(),
        restaurant.getAvgRating(), reviewDTOS, restaurant.getZone().getId());
    }


//===============================================================================================================//



    @Data
    @AllArgsConstructor
    static class RestaurantDetailDTO {
        private String name;
        private String mainPhotoUrl;
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

