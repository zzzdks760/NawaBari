package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.review.Photo;
import com.backend.NawaBari.service.ReviewService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping("/api/createReview")
    public ReviewResponseDTO createReview(@RequestBody @Validated ReviewRequestDTO request) {
        Long reviewId = reviewService.createReview(request.getMemberId(), request.getRestaurantId(), request.getPhotos(), request.getTitle(), request.getContent(), request.getRate());

        return new ReviewResponseDTO(reviewId);
    }

    @Data
    private class ReviewResponseDTO {
        private Long reviewId;

        public ReviewResponseDTO(Long reviewId) {
            this.reviewId = reviewId;
        }
    }

    @Data
    private class ReviewRequestDTO {
        private Long memberId;
        private Long restaurantId;
        private List<Photo> photos;
        private String title;
        private String content;
        private Double rate;
    }

}
