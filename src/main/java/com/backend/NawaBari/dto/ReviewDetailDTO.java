package com.backend.NawaBari.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<String> photoList;
    private String time;

}
