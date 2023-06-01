package com.backend.NawaBari.api;

import com.backend.NawaBari.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartApiController {

    private final HeartService heartService;

    @PostMapping("/api/v1/restaurants/{restaurantId}/reviews/{reviewId}/hearts")
    public ResponseEntity<Boolean> addHeart(@PathVariable("restaurantId") Long restaurantId,
                                            @PathVariable("reviewId") Long reviewId,
                                            @RequestParam("memberId") Long memberId) {
        Boolean isLiked = heartService.addHeart(memberId, reviewId);
        return ResponseEntity.ok(isLiked);
    }
}
