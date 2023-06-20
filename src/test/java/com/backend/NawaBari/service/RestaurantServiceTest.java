package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void 통합검색() throws Exception {
        //given
        Restaurant restaurant1 = Restaurant.builder()
                .name("미분당")
                .address_name("서울특별시 강남구 역삼동 148-1")
                .lat(123.123)
                .lng(456.456)
                .build();
        Restaurant restaurant2 = Restaurant.builder()
                .name("미소야(역삼점)")
                .address_name("서울특별시 강남구 논현동 527-96")
                .lat(123.456)
                .lng(456.123)
                .build();

        em.persist(restaurant1);
        em.persist(restaurant2);

        String inputName1 = "미소야";
        String inputName2 = "서울";
        String inputName3 = "미";


        //when

//        List<Restaurant> restaurants1 = restaurantService.searchByNameAndAddress(inputName1);
//        List<Restaurant> restaurants2 = restaurantService.searchByNameAndAddress(inputName2);
//
//        assertThat(restaurants1.size()).isEqualTo(1);
//        assertThat(restaurants2.size()).isEqualTo(2);
        assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.findByRestaurantName(inputName3);
        });

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
        String inputName2 = "김밥";
        String inputName3 = "김";


        //when

        List<Restaurant> restaurants1 = restaurantService.findByRestaurantName(inputName1);
        List<Restaurant> restaurants2 = restaurantService.findByRestaurantName(inputName2);

        //then
        assertThat(restaurants1.size()).isEqualTo(2);
        assertThat(restaurants2.size()).isEqualTo(2);
        assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.findByRestaurantName(inputName3);
        });

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
        String inputName2 = "서";
        String inputName3 = "반포동";

        //when

        List<Restaurant> address1 = restaurantService.findByAddressName(inputName1);
        List<Restaurant> address3 = restaurantService.findByAddressName(inputName3);


        //then
        assertThat(address1.size()).isEqualTo(3);
        assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.findByAddressName(inputName2);
        });
        assertThat(address3.size()).isEqualTo(1);
    }

    @Test
    public void 식당상세조회() throws Exception {
        //given
        Restaurant restaurant = Restaurant.builder()
                .name("마루심")
                .address_name("서초구 반포동 54-10")
                .lat(123.123)
                .lng(456.456)
                .reviews(new ArrayList<>())
                .build();
        em.persist(restaurant);

        Review review1 = Review.builder()
                .title("Good")
                .likeCount(18)
                .build();

        Review review2 = Review.builder()
                .title("fuck")
                .likeCount(3)
                .build();

        Review review3 = Review.builder()
                .title("soso")
                .likeCount(4)
                .build();

        Review review4 = Review.builder()
                .title("bad")
                .likeCount(1)
                .build();

        restaurant.addReview(review1);
        restaurant.addReview(review2);
        restaurant.addReview(review3);
        restaurant.addReview(review4);

        em.persist(restaurant);
        em.persist(review1);
        em.persist(review2);
        em.persist(review3);
        em.persist(review4);
        em.flush();

        //when
        Restaurant detail = restaurantService.detailRestaurant(restaurant.getId());

        //then
        List<Review> top3Reviews = detail.getReviews();
        assertThat(detail.getReviews().size()).isEqualTo(3);
        assertThat(top3Reviews).containsExactly(review1, review3, review2);
    }

    @Test
    public void 통합검색_페이징처리() throws Exception {
        //given

        String keyword = "반포동";
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);


        //when
        Slice<Restaurant> restaurants = restaurantRepository.searchByKeywordContaining(keyword, pageRequest);


        //then
        assertNotNull(restaurants);
    }

}