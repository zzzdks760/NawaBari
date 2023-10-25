package com.backend.NawaBari.api;


import com.backend.NawaBari.service.HeartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HeartApiController {

    private final HeartService heartService;

    @PostMapping("/api/v1/restaurant/review/heart")
    public int heartCount(@RequestBody HeartRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        Long reviewId = requestDTO.getReviewId();
        return heartService.toggleHeart(memberId, reviewId);
    }

    @Getter
    static class HeartRequestDTO {
        private Long memberId;
        private Long reviewId;
    }
}
