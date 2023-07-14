package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.ReviewRepository;
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

    @PostMapping("/api/v1/reviews/{reviewId}/{memberId}/hearts")
    public int heartCount(@PathVariable("reviewId") Long reviewId, @PathVariable("memberId") Long memberId) {
        int count = heartService.toggleHeart(memberId, reviewId);
        return count;
    }
}
