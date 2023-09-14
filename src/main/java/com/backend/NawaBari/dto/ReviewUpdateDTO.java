package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDTO {
    private Long reviewId;
    private String title;
    private String content;
    private List<Photo> photos;
    private Double rate;
    private String time;

    public static ReviewUpdateDTO convertToDTO(Review review) {
        ReviewUpdateDTO reviewDTO = new ReviewUpdateDTO();
        reviewDTO.setReviewId(review.getId());
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setTime(review.getFormattedTime());

        return reviewDTO;
    }
}
