package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.domain.review.Photo;
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
public class ReviewDTO {
    private Long id;
    private String title;
    private String content;
    private Double rate;
    private List<Photo> photos;
    private Member writer;
    private Restaurant restaurant;

}
