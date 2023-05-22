package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.review.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    //리뷰등록
    @PostMapping("/api/restaurant/createReview")
    public ReviewResponseDTO createReview(@RequestBody @Validated ReviewRequestDTO request) {
        Long reviewId = reviewService.createReview(request.getMemberId(), request.getRestaurantId(), request.getPhotos(), request.getTitle(), request.getContent(), request.getRate());

        return new ReviewResponseDTO(reviewId);
    }

    //리뷰수정
    @PutMapping("/api/restaurant/review/{id}")
    public UpdateReviewResponse updateReview(
            @PathVariable("id") Long id,
            @RequestBody @Validated UpdateReviewRequest request) {

        reviewService.updateReview(id, request.getPhotos(), request.getTitle(), request.getContent(), request.getRate());
        Review findReview = reviewService.findOne(id);
        return new UpdateReviewResponse(findReview.getId(), findReview.getPhotos(), findReview.getTitle(), findReview.getContent(), findReview.getRate());
    }

    //리뷰삭제
    @DeleteMapping("/api/{restaurantId}/restaurant/{reviewId}")
    public void deleteReview(@PathVariable Long restaurantId,
                             @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(restaurantId, reviewId);
    }

    //리뷰조회
    @GetMapping("/api/{restaurantId}/reviews")
    public List<ReviewDTO> RestaurantReviews(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.findReviewList(restaurantId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();

        for (Review review : reviews) {
            ReviewDTO reviewDTO = new ReviewDTO(
                    review.getId(),
                    review.getPhotos(),
                    review.getTitle(),
                    review.getContent(),
                    review.getRate(),
                    review.getLikeCount()
            );
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }


    //===============================================================================================================//

    @Data
    static class ReviewResponseDTO {
        private Long reviewId;

        public ReviewResponseDTO(Long reviewId) {
            this.reviewId = reviewId;
        }
    }

    @Data
    static class ReviewRequestDTO {
        private Long memberId;
        private Long restaurantId;
        private List<Photo> photos;
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    @AllArgsConstructor
    static class UpdateReviewResponse {
        private Long id;
        private List<Photo> photos;
        private String title;
        private String content;
        private Double rate;

    }

    @Data
    static class UpdateReviewRequest {
        private List<Photo> photos;
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    static class ReviewDTO {
        private Long id;
        private List<Photo> photos;
        private String title;
        private String content;
        private Double rate;
        private int likeCount;

        public ReviewDTO(Long id, List<Photo> photos, String title, String content, Double rate, int likeCount) {
            this.id = id;
            this.photos = photos;
            this.title = title;
            this.content = content;
            this.rate = rate;
            this.likeCount = likeCount;
        }
    }
}
