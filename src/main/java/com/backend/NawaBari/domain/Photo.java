package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @Column(nullable = false)
    private String file_name;

    @Column(nullable = false)
    private String file_path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    //==연관관계 설정==//
//    public void setReview(Review review) {
//        this.review = review;
//    }

}
