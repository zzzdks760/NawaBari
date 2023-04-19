package com.backend.NawaBari.converter;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.dto.RestaurantDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class RestaurantDTOConverter {

    public List<RestaurantDTO> toDTOList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> {
                    RestaurantDTO restaurantDTO = new RestaurantDTO();
                    restaurantDTO.setId(r.getId());
                    restaurantDTO.setName(r.getName());
                    restaurantDTO.setAvgRating(r.getAvgRating());
                    restaurantDTO.setReviewCount(r.getReviewCount());
                    restaurantDTO.setRestaurant_img(r.getRestaurant_img());
                    return restaurantDTO;
                })
                .collect(Collectors.toList());

    }
}
