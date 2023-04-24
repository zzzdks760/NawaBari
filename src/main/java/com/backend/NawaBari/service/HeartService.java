package com.backend.NawaBari.service;


import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.HeartRepository;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public Boolean addHeart(Long memberId, Long reviewId) {

        Member member = memberRepository.findOne(memberId);
        Review review = reviewRepository.findOne(reviewId);

        Heart heart = heartRepository.findLiked(member, review);
        //좋아요 처음 누른경우
        if (heart == null){
            heart = Heart.createHeart(member, review, false);
            heart.like();
        }
        //좋아요가 false인지 true인지
        if (!heart.isLiked()) {
            heart.like();
        } else {
            heart.unlike();
        }
        heartRepository.save(heart);
        return heart.isLiked();
    }
}
