package com.backend.NawaBari.domain.review;

import com.backend.NawaBari.domain.Base;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String title;

    private String content;

    private Double rate;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.writer = member;
        member.getReviews().add(this);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setReview(this);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReviews().add(this);
    }




    public static Review toReviewSaveEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setPhotos(reviewDTO.getPhotos());
        review.setRestaurant(reviewDTO.getRestaurant());

        return review;
    }

}

