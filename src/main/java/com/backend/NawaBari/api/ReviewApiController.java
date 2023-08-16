package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    //리뷰등록
    @PostMapping("/api/v1/restaurants/reviews")
    public ReviewResponseDTO createReview(@RequestBody @Validated ReviewRequestDTO request) {

        Long reviewId = reviewService.createReview(request.getMemberId(), request.getRestaurantId(),
                request.getTitle(), request.getContent(), request.getRate());
        return new ReviewResponseDTO(reviewId);

    }

    //리뷰수정
    @PatchMapping("/api/v1/restaurants/reviews")
    public UpdateReviewResponse updateReview(@RequestBody UpdateReviewRequestDTO updateRequest) {

        reviewService.updateReview(updateRequest.getReviewId(), updateRequest.getRestaurantId(),
                updateRequest.getTitle(), updateRequest.getContent(), updateRequest.getRate());

        return new UpdateReviewResponse(updateRequest.getReviewId(),
                updateRequest.getTitle(), updateRequest.getContent(), updateRequest.getRate());
    }

    //리뷰삭제
    @DeleteMapping("/api/v1/restaurants/reviews")
    public ResponseEntity<Boolean> deleteReview(@RequestBody DeleteReviewRequestDTO deleteRequest) {
        Long reviewId = deleteRequest.getReviewId();
        Long restaurantId = deleteRequest.getRestaurantId();
        Boolean isDeleted = reviewService.deleteReview(reviewId, restaurantId);

        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //특정 식당 리뷰 전체 조회
    @GetMapping("/api/v1/restaurant/reviews")
    public Slice<ReviewDTO> RestaurantReviews(@RequestBody AllReviewRequestDTO allReviewRequest, @PageableDefault Pageable pageable) {
        Long restaurantId = allReviewRequest.getRestaurantId();

        Slice<Review> reviews = reviewService.findAllReview(restaurantId, pageable);

        List<ReviewDTO> reviewDTOS = new ArrayList<>();



        for (Review review : reviews) {

            ReviewDTO reviewDTO = new ReviewDTO(
                    review.getId(),
                    review.getTitle(),
                    review.getContent(),
                    review.getRate(),
                    review.getLikeCount()
            );
            reviewDTOS.add(reviewDTO);
        }
        return new SliceImpl<>(reviewDTOS, reviews.getPageable(), reviews.hasNext());
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
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    @AllArgsConstructor
    static class UpdateReviewResponse {
        private Long reviewId;
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    static class UpdateReviewRequest {
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    @AllArgsConstructor
    static class ReviewDTO {
        private Long id;
        private String title;
        private String content;
        private Double rate;
        private int likeCount;
    }

    @Data
    static class UpdateReviewRequestDTO {
        private Long reviewId;
        private Long restaurantId;
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    static class DeleteReviewRequestDTO {
        private Long reviewId;
        private Long restaurantId;
    }

    @Data
    static class AllReviewRequestDTO {
        private Long restaurantId;
    }
}
