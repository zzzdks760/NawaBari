package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.review.Review;
import lombok.*;

import java.util.ArrayList;
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
    private List<PhotoInfo> photos;
    private String time;


    public static ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getId());
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setLikeCount(review.getLikeCount());
        reviewDTO.setTime(review.getFormattedTime());

        List<PhotoInfo> photoInfoList = new ArrayList<>();

        if (review.getPhotos() != null) {
            for (Photo photo : review.getPhotos()) {
                String photoUrl = photo.getFile_path();
                Long photoId = photo.getId();
                PhotoInfo photoInfo = new PhotoInfo(photoId, photoUrl);
                photoInfoList.add(photoInfo);
            }
        }
        reviewDTO.setPhotos(photoInfoList);

        return reviewDTO;
    }


}
