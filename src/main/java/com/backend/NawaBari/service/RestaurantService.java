package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ZoneRepository zoneRepository;


    //식당 생성
    @Transactional
    public Long createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }


    //통합 검색
//    public Slice<Restaurant> searchByNameAndAddress(String keyword, Pageable pageable) {
//        if (keyword.length() < 2) {
//            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
//        }
//        Slice<Restaurant> restaurants = restaurantRepository.searchByNameAndAddress(keyword, pageable);
//
//        if (restaurants.getContent().isEmpty()) {
//            restaurants = restaurantRepository.searchByKeywordContaining(keyword, pageable);
//        }
//
//        if(restaurants.getContent().isEmpty()) {
//            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
//        }
//
//        return restaurants;
//    }

    //상호명 검색
        public Slice<Restaurant> searchByName(String name, Pageable pageable) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByName(name, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByNameContaining(name, pageable);
        }

        if(restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        return restaurants;
    }

    //주소 검색
    public Slice<Restaurant> searchByAddress(String address, Pageable pageable) {
        if (address.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByAddress(address, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByAddressContaining(address, pageable);
        }

        if(restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        return restaurants;
    }

    //현재 동 위치 식당리스트 조회
    public Slice<Restaurant> searchByCurrentRestaurant(String dongName, Pageable pageable) {
        return restaurantRepository.searchByDongName(dongName, pageable);
    }


    //식당 상세조회
    public Restaurant detailRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        List<Review> reviewTop3 = restaurantRepository.findReviewTop3(restaurantId);
        Zone zone = restaurant.getZone();


        Restaurant restaurantDetail = Restaurant.builder()
                .name(restaurant.getName())
                .main_photo_fileName(restaurant.getMain_photo_fileName())
                .address_name(restaurant.getAddress_name())
                .lat(restaurant.getLat())
                .lng(restaurant.getLng())
                .reviewCount(restaurant.getReviewCount())
                .tel(restaurant.getTel())
                .reviews(reviewTop3)
                .zone(zone)
                .avgRating(restaurant.getAvgRating())
                .build();
        return restaurantDetail;
    }


    //식당 수정
    @Transactional
    public void updateRestaurant(Long restaurantId, String name, String mainPhotoFileName, String openingTime, String closingTime, String address_name, String tel) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        restaurant.update(name, mainPhotoFileName, openingTime, closingTime, address_name, tel);
    }


    //식당 삭제
    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }

}