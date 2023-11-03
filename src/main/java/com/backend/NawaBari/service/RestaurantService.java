package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Menu;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.*;
import com.backend.NawaBari.repository.BookMarkRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final BookMarkRepository bookMarkRepository;


    //식당 생성
    @Transactional
    public Long createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }


    //통합 검색
    public Slice<RestaurantDTO> searchByNameAndAddress(String keyword, Pageable pageable) {
        if (keyword.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByNameAndAddress(keyword, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByKeywordContaining(keyword, pageable);
        }

        if(restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        // 한줄평 포함한 RestaurantDTO 생성
        for (Restaurant restaurant : restaurants.getContent()) {

            Review topReview = reviewRepository.findTopReviewTitle(restaurant.getId());

            String topReviewTitle = null;
            if (topReview != null) {
                topReviewTitle = topReview.getTitle();
            }

            RestaurantDTO restaurantDTO = RestaurantDTO.convertToDTO(restaurant);
            restaurantDTO.setTopReviewTitle(topReviewTitle);
            restaurantDTOS.add(restaurantDTO);

        }

        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }


    //리뷰작성시 식당검색
    public Slice<RestaurantSearchDTO> Search (String keyword, Pageable pageable) {
        if (keyword.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByNameAndAddress(keyword, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByKeywordContaining(keyword, pageable);
        }

        if(restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        List<RestaurantSearchDTO> restaurantSearchDTOS = new ArrayList<>();

        for (Restaurant restaurant : restaurants.getContent()) {
            RestaurantSearchDTO restaurantSearchDTO = RestaurantSearchDTO.convertToDTO(restaurant);
            restaurantSearchDTOS.add(restaurantSearchDTO);
        }

        return new SliceImpl<>(restaurantSearchDTOS, restaurants.getPageable(), restaurants.hasNext());
    }

    //상호명 검색
    /*    public Slice<RestaurantDTO> searchByName(String name, Pageable pageable) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByName(name, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByNameContaining(name, pageable);
        }

        if (restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        // 한줄평 포함한 RestaurantDTO 생성
        for (Restaurant restaurant : restaurants.getContent()) {

            Review topReview = reviewRepository.findTopReviewTitle(restaurant.getId());

            String topReviewTitle = null;
            if (topReview != null) {
                topReviewTitle = topReview.getTitle();
            }

            RestaurantDTO restaurantDTO = RestaurantDTO.convertToDTO(restaurant);
            restaurantDTO.setTopReviewTitle(topReviewTitle);
            restaurantDTOS.add(restaurantDTO);

        }

        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }*/



    //주소 검색
    /*public Slice<RestaurantDTO> searchByAddress(String address, Pageable pageable) {
        if (address.length() < 2) {
            throw new IllegalArgumentException("최소 두 글자 이상 입력해 주세요.");
        }
        Slice<Restaurant> restaurants = restaurantRepository.searchByAddress(address, pageable);

        if (restaurants.getContent().isEmpty()) {
            restaurants = restaurantRepository.searchByAddressContaining(address, pageable);
        }

        if (restaurants.getContent().isEmpty()) {
            throw new IllegalArgumentException("일치하는 식당이 없습니다.");
        }

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        // 한줄평 포함한 RestaurantDTO 생성
        for (Restaurant restaurant : restaurants.getContent()) {

            Review topReview = reviewRepository.findTopReviewTitle(restaurant.getId());

            String topReviewTitle = null;
            if (topReview != null) {
                topReviewTitle = topReview.getTitle();
            }

            RestaurantDTO restaurantDTO = RestaurantDTO.convertToDTO(restaurant);
            restaurantDTO.setTopReviewTitle(topReviewTitle);
            restaurantDTOS.add(restaurantDTO);

        }

        return new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext());
    }*/


    //현재 동 위치 식당리스트 조회 (메인페이지)
    public Slice<MainPageDTO> searchByCurrentRestaurant(String guName, String dongName, Pageable pageable) {
        Slice<Restaurant> restaurants = restaurantRepository.searchByDongName(dongName, pageable);

        List<MainPageDTO> mainPageDTOS = new ArrayList<>();
        // 한줄평 포함한 RestaurantDTO 생성
        for (Restaurant restaurant : restaurants.getContent()) {

            Review topReview = reviewRepository.findTopReviewTitle(restaurant.getId());

            String topReviewTitle = null;
            if (topReview != null) {
                topReviewTitle = topReview.getTitle();
            }

            MainPageDTO mainPageDTO = MainPageDTO.convertToDTO(restaurant);
            mainPageDTO.setTopReviewTitle(topReviewTitle);
            mainPageDTO.setGuName(guName);
            mainPageDTO.setDongName(dongName);
            mainPageDTOS.add(mainPageDTO);
        }

        return new SliceImpl<>(mainPageDTOS, restaurants.getPageable(), restaurants.hasNext());
    }


    //식당 상세조회
    public RestaurantDetailDTO detailRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        List<Review> reviewTop3 = restaurantRepository.findReviewTop3(restaurantId);
        List<Long> bookMarks = bookMarkRepository.findBookMarkMember(restaurantId);
        List<Menu> menus = restaurantRepository.findMenu(restaurantId);

        List<ReviewDTO> top3Review = reviewTop3.stream()
                .map(ReviewDTO::convertToDTO)
                .collect(Collectors.toList());

        List<MenuDTO> menuDTOS = menus.stream()
                .map(MenuDTO::convertToDTO)
                .collect(Collectors.toList());

        RestaurantDetailDTO restaurantDetailDTO = RestaurantDetailDTO.convertToDTO(restaurant, bookMarks, top3Review, menuDTOS);

        return  restaurantDetailDTO;
    }

}