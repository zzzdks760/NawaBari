package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewDTO {
    private Long reviewId;
    private String title;
    private int likeCount;
    private List<PhotoDTO> photoDTOS;
    private String restaurantName;

    public static MyReviewDTO convertToDTO (Review review) {
        MyReviewDTO myReviewDTO = new MyReviewDTO();
        myReviewDTO.setReviewId(review.getId());
        myReviewDTO.setTitle(review.getTitle());
        myReviewDTO.setLikeCount(review.getLikeCount());
        myReviewDTO.setRestaurantName(review.getRestaurant().getName());
        return myReviewDTO;
    }

}
