package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailDTO {
    private Long restaurantId; //식당
    private Double avgRating; //식당

    private Long writerId; //회원
    private List<Long> likeMember; //회원

    private String title; //리뷰
    private String content; //리뷰
    private Double rate; //리뷰
    private int likeCount; //리뷰
    private List<PhotoInfo> photos;
    private String time;

    public void setPhotos(List<Photo> photos) {
        this.photos = photos.stream()
                .map(photo -> new PhotoInfo(photo.getId(), photo.getFile_path()))
                .collect(Collectors.toList());
    }
}
