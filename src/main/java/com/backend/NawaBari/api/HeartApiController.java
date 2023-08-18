package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import com.backend.NawaBari.service.HeartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Data
    static class HeartRequestDTO {
        private Long memberId;
        private Long reviewId;
    }
}
