package com.backend.NawaBari.api;

import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.service.CurrentLocationService;
import com.backend.NawaBari.service.RestaurantService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrentLocationApiController {
    private final CurrentLocationService currentLocationService;
    private final RestaurantService restaurantService;

    /**
     * 현위치를 받아와 동 이름이 일치하는 식당들 조회 (메인페이지)
     */
    @PostMapping("/api/v1/location/restaurants")
    public Slice<RestaurantDTO> getCurrentLocation(@RequestBody LocationDTO locationDTO,
                                                                   @PageableDefault Pageable pageable) {
        float current_lat = locationDTO.getCurrent_lat();
        float current_lng = locationDTO.getCurrent_lng();
        String dongName = currentLocationService.getCurrentLocation(current_lat, current_lng);

        //동 이름이 없을경우 default 값으로 강남을 설정
        if (dongName == null) {
            dongName = "논현동";
        }
        return restaurantService.searchByCurrentRestaurant(dongName, pageable);
    }


    @Data
    static class LocationDTO {
        private float current_lat;
        private float current_lng;
    }

}
