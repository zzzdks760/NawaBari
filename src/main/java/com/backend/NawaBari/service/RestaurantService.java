package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;


    //식당 생성
    @Transactional
    public Long createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    //식당 이름으로 조회
    public List<Restaurant> findByRestaurantName(String name) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 필요합니다.");
        }
        List<Restaurant> restaurants = restaurantRepository.findByName(name);
        if (restaurants.size() == 0) { //일치하는 식당이 없는 경우
            //입력된 이름에서 최소 두 글자 이상이 연속된 문자가 같은 경우를 찾습니다.
            for (int i = 0; i < name.length() - 1; i++) {
                String sub = name.substring(i, i + 2);
                List<Restaurant> temp = restaurantRepository.findByName(sub);
                if (temp.size() > 0) { //일치하는 식당이 있는 경우
                    return temp;
                }
            }
        }
        return restaurants;
    }

    //주소로 식당 조회
    public List<Restaurant> findByAddressName(String addressName) {
        if (addressName.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 필요합니다.");
        }
        if (addressName.contains(" ")) { // 입력된 주소에 공백이 포함된 경우
            String[] tokens = addressName.split(" ");
            // 서초구와 같이 동이 아닌 시,군,구 단위로 입력된 경우
            if (tokens.length == 1) {
                return restaurantRepository.findRestaurantByAddress(tokens[0]);
            } else { // 서초구 양재동과 같이 동까지 입력된 경우
                String regex = "(?i).*\\b" + tokens[1] + "\\b.*"; // 대소문자 구분 없이 입력된 동이 단어 경계 내에서 포함되는지 확인하는 정규식
                return restaurantRepository.findRestaurantByAddress(tokens[0])
                        .stream()
                        .filter(r -> r.getAddress_name().matches(regex))
                        .collect(Collectors.toList());
            }
        } else { // 입력된 주소가 시,군,구 단위로 입력된 경우
            return restaurantRepository.findRestaurantByAddress(addressName);
        }
    }

    //통합 검색
    public Slice<Restaurant> searchByNameAndAddress(String keyword, Pageable pageable) {
        if (keyword.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByNameAndAddress(keyword, pageable);

        if (restaurants.isEmpty()) {
            restaurants = restaurantRepository.searchByKeywordContaining(keyword, pageable);
        }

        return restaurants;
    }



    //식당 상세조회
    public Restaurant detailRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        List<Review> reviewTop3 = restaurantRepository.findReviewTop3(restaurantId);

        Restaurant restaurantDetail = Restaurant.builder()
                .name(restaurant.getName())
                .address_name(restaurant.getAddress_name())
                .lat(restaurant.getLat())
                .lng(restaurant.getLng())
                .reviewCount(restaurant.getReviewCount())
                .tel(restaurant.getTel())
                .reviews(reviewTop3)
                .build();
        return restaurantDetail;
    }


    //식당 수정
    @Transactional
    public void updateRestaurant(Long restaurantId, String name, String restaurant_img, String openingTime, String closingTime, String address_name, String tel) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        restaurant.update(name, restaurant_img, openingTime, closingTime, address_name, tel);
    }


    //식당 삭제
    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }
}
