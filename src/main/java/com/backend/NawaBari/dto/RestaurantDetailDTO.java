package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.domain.Menu;
import com.backend.NawaBari.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailDTO {
    //식당 데이터
    private String name;
    private String main_photo_url;
    private int bookMarkCount;
    private double avgRating;
    private int reviewCount;
    private String holidays;
    private String tel;
    private String opening_hours;
    private Long zoneId;
    //북마크 회원 아이디 리스트
    private List<Long> bookMarkMembers;
    //리뷰 TOP3
    private List<ReviewDTO> reviewTop3;
    //메뉴 리스트
    private List<MenuDTO> menus;

    public static RestaurantDetailDTO convertToDTO(Restaurant restaurant, List<Long> bookMarkMembers, List<ReviewDTO> reviewTop3, List<MenuDTO> menus) {

        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO();

        restaurantDetailDTO.setName(restaurant.getName());
        restaurantDetailDTO.setMain_photo_url(restaurant.getMain_photo_url());
        restaurantDetailDTO.setBookMarkCount(restaurant.getBookMarkCount());
        restaurantDetailDTO.setAvgRating(restaurant.getAvgRating());
        restaurantDetailDTO.setReviewCount(restaurant.getReviewCount());
        restaurantDetailDTO.setHolidays(restaurant.getHolidays());
        restaurantDetailDTO.setTel(restaurant.getTel());
        restaurantDetailDTO.setOpening_hours(restaurant.getOpening_hours());
        restaurantDetailDTO.setZoneId(restaurant.getZone().getId());
        restaurantDetailDTO.setBookMarkMembers(bookMarkMembers);
        restaurantDetailDTO.setMenus(menus);
        restaurantDetailDTO.setReviewTop3(reviewTop3);

        return restaurantDetailDTO;
    }
}
