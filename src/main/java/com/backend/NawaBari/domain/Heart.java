package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private boolean liked;

    //== 생성 메서드 ==//
    public static Heart createHeart(Member member, Review review, Boolean liked) {
        Heart heart = new Heart();
        heart.setMember(member);
        heart.setReview(review);
        heart.setLiked(false);
        return heart;
    }

    public void like() {
        if (!this.liked){
            this.liked = true;
            this.review.addHeart(this);
        } else {
            this.unlike();
        }
    }

    public void unlike() {
        if (this.liked) {
            this.liked = false;
            this.review.removeHeart(this);
        }
    }


    @Builder
    public Heart(Member member, Review review, boolean liked) {
        this.member = member;
        this.review = review;
        this.liked = liked;
    }


}
