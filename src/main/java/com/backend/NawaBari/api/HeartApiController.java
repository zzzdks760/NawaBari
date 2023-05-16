package com.backend.NawaBari.api;

import com.backend.NawaBari.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartApiController {

    private final HeartService heartService;

    @PostMapping("/api/heart")
    public ResponseEntity<Boolean> addHeart(@RequestParam Long memberId, @RequestParam Long reviewId) {
        Boolean isLiked = heartService.addHeart(memberId, reviewId);
        return ResponseEntity.ok(isLiked);
    }
}
