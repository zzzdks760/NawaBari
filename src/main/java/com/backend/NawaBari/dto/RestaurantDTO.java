package com.backend.NawaBari.dto;


import com.backend.NawaBari.domain.Menu;
import com.backend.NawaBari.domain.Restaurant;
import lombok.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long restaurantId;
    private String name;
    private String address_name;
    private String tel;
    private String opening_hours;
    private String holidays;
    private String main_photo_url;
    private int reviewCount;
    private Double avgRating;
    private Map<String, String> menu;
    private Long zoneId;
    private int bookMarkCount;
    private String topReviewTitle;

    //식당데이터 삽입
    public Restaurant toEntity() {
        Restaurant restaurant = Restaurant.builder()
                .name(this.name)
                .address_name(this.address_name)
                .tel(this.tel)
                .opening_hours(this.opening_hours)
                .holidays(this.holidays)
                .main_photo_url(this.main_photo_url)
                .build();

        // Menu 엔터티 생성 및 매핑
        List<Menu> menus = this.menu.entrySet().stream()
                .map(entry -> {
                    MenuDTO menuDTO = new MenuDTO(entry.getKey(), entry.getValue());
                    return Menu.builder()
                            .name(menuDTO.getName())
                            .price(menuDTO.getPrice())
                            .restaurant(restaurant)
                            .build();
                })
                .collect(Collectors.toList());

        restaurant.setMenus(menus); // Restaurant 엔터티에 메뉴 데이터 추가

        return restaurant;
    }


    public static RestaurantDTO convertToDTO(Restaurant restaurant) {

            RestaurantDTO restaurantDTO = new RestaurantDTO();

            restaurantDTO.setRestaurantId(restaurant.getId());
            restaurantDTO.setName(restaurant.getName());
            restaurantDTO.setAddress_name(restaurant.getAddress_name());
            restaurantDTO.setMain_photo_url(restaurant.getMain_photo_url());
            restaurantDTO.setReviewCount(restaurant.getReviewCount());
            restaurantDTO.setAvgRating(restaurant.getAvgRating());
            restaurantDTO.setBookMarkCount(restaurant.getBookMarkCount());
            restaurantDTO.setZoneId(restaurant.getZone().getId());

            return restaurantDTO;
    }
}
