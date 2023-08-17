package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.review.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private String title;
    private String content;
    private Double rate;
    private int likeCount;


    public static ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getId());
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setLikeCount(review.getLikeCount());
        return reviewDTO;
    }


}
