package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    //식당 이름 조회
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
    public List<Restaurant> findByAddressName(String address_name) {
        if (address_name.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 필요합니다.");
        }
        return restaurantRepository.findRestaurantByAddress(address_name);

    }

    //식당 수정
    @Transactional
    public void updateRestaurant(Long restaurantId, String name, String restaurant_img, LocalTime openingTime, LocalTime closingTime, String address_name, String tel) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        restaurant.setName(name);
        restaurant.setRestaurant_img(restaurant_img);
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setAddress_name(address_name);
        restaurant.setTel(tel);
    }


    //식당 삭제
    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }
}
