package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class RestaurantServiceTest {

    @Autowired RestaurantService restaurantService;
    @Autowired RestaurantRepository restaurantRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 식당생성() throws Exception {
        //given
        Restaurant restaurant = Restaurant.builder()
                .name("김밥천국")
                .address_name("강남구")
                .lat(123.123)
                .lng(456.456)
                .build();
        em.persist(restaurant);

        //when
        Long restaurantId = restaurantService.createRestaurant(restaurant);

        Restaurant findRestaurant = restaurantRepository.findOne(restaurantId);

        //then
        assertThat(findRestaurant.getId()).isEqualTo(restaurantId);
    }

    @Test
    public void 식당이름으로조회() throws Exception {
        //given
        Restaurant restaurant1 = Restaurant.builder()
                .name("김밥 천국 강남점")
                .address_name("강남구")
                .lat(123.123)
                .lng(456.456)
                .build();
        Restaurant restaurant2 = Restaurant.builder()
                .name("김밥 천국 중구점")
                .address_name("중구")
                .lat(123.456)
                .lng(456.123)
                .build();

        em.persist(restaurant1);
        em.persist(restaurant2);

        String inputName1 = "김밥천국";
        String inputName2 = "천국";


        //when

        List<Restaurant> restaurants = restaurantService.findByRestaurantName(inputName2);

        //then
        assertThat(restaurants.size()).isEqualTo(2);
    }

    @Test
    public void 주소로식당조회() throws Exception {
        //given
        Restaurant restaurant1 = Restaurant.builder()
                .name("마루심")
                .address_name("서초구 반포동 54-10")
                .lat(123.123)
                .lng(456.456)
                .build();
        Restaurant restaurant2 = Restaurant.builder()
                .name("메종조")
                .address_name("서초구 서초동 1476-10")
                .lat(123.456)
                .lng(456.123)
                .build();
        Restaurant restaurant3 = Restaurant.builder()
                .name("서관면옥")
                .address_name("서초구 서초동 1675-5")
                .lat(123.456)
                .lng(456.123)
                .build();

        em.persist(restaurant1);
        em.persist(restaurant2);
        em.persist(restaurant3);

        String inputName1 = "서초구";
        String inputName2 = "서초";
        String inputName3 = "반포동";

        //when

        List<Restaurant> address = restaurantService.findByAddressName(inputName3);

        //then
        assertThat(address.size()).isEqualTo(1);

    }


}