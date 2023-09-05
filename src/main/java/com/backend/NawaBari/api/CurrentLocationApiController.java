package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.service.CurrentLocationService;
import com.backend.NawaBari.service.RestaurantService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CurrentLocationApiController {
    private final CurrentLocationService currentLocationService;
    private final RestaurantService restaurantService;

    /**
     * 현위치를 받아와 동 이름이 일치하는 식당들 조회
     */
    @PostMapping("/api/v1/location/restaurants")
    public ResponseEntity<Slice<RestaurantDTO>> getCurrentLocation(@RequestBody LocationDTO locationDTO,
                                                                   @PageableDefault Pageable pageable) {
        float current_lat = locationDTO.getCurrent_lat();
        float current_lng = locationDTO.getCurrent_lng();
        String dongName = currentLocationService.getCurrentLocation(current_lat, current_lng);

        //동 이름이 없을경우 404 Not Found 반환
        if (dongName == null) {
            return ResponseEntity.notFound().build();
        }

        Slice<Restaurant> restaurants = restaurantService.searchByCurrentRestaurant(dongName, pageable);

        List<RestaurantDTO> restaurantDTOS = restaurants.getContent().stream()
                .map(RestaurantDTO::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new SliceImpl<>(restaurantDTOS, restaurants.getPageable(), restaurants.hasNext()));
        //content가 null일 경우 동과 일치하는 주소가 없는것 = 서울 이외의 지역인 경우
    }


    @Data
    static class LocationDTO {
        private float current_lat;
        private float current_lng;
    }

}
