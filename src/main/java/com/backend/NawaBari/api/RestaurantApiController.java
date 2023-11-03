package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.dto.RestaurantDetailDTO;
import com.backend.NawaBari.dto.RestaurantSearchDTO;
import com.backend.NawaBari.dto.ReviewDTO;
import com.backend.NawaBari.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

        return restaurantService.searchByNameAndAddress(keyword, pageable);
    }

    //리뷰 작성시 식당 검색
    @GetMapping("/api/v1/restaurants/review/search")
    public Slice<RestaurantSearchDTO> Search(@RequestParam("keyword") String keyword, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return restaurantService.Search(keyword, pageable);
    }


//    //주소 검색
//    @GetMapping("/api/v1/restaurants/search/address")
//    public Slice<RestaurantDTO> addressSearch(@RequestParam("address") String address, @PageableDefault(size = 10, page = 0) Pageable pageable) {
//
//        return restaurantService.searchByAddress(address, pageable);
//
////        List<RestaurantDTO> restaurantDTOS = restaurants.getContent().stream()
////                .map(RestaurantDTO::convertToDTO)
////                .collect(Collectors.toList());
////
////        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
//    }
//
//    //상호명 검색
//    @GetMapping("/api/v1/restaurants/search/name")
//    public Slice<RestaurantDTO> nameSearch(@RequestParam("name") String name, @PageableDefault(size = 10, page = 0) Pageable pageable) {
//
//        return restaurantService.searchByName(name, pageable);
//    }


    //식당 상세조회
    @GetMapping("/api/v1/restaurants/restaurant")
    public RestaurantDetailDTO RestaurantDetail(@RequestParam("restaurantId") Long restaurantId) {

        return restaurantService.detailRestaurant(restaurantId);

    }
}

