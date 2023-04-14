package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;


    //식당 생성
    public Long createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    //식당 이름 조회

    //행정구역 코드로 식당 조회

    //식당 수정
    public void updateRestaurant(Long restaurantId, String name, String restaurant_img, LocalTime openingTime, LocalTime closingTime, String location, String tel) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        restaurant.setName(name);
        restaurant.setRestaurant_img(restaurant_img);
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setLocation(location);
        restaurant.setTel(tel);
    }


    //식당 삭제
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }
}
