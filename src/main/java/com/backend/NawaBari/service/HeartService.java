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

    @Transactional
    public int toggleHeart(Long memberId, Long reviewId) {
        //회원과 리뷰를 DB에서 찾아서 Heart객체가 존재하는지 체크(존재한다 == true)
        Member member = memberRepository.findOne(memberId);
        Review review = reviewRepository.findOne(reviewId);
        Heart heart = heartRepository.findLiked(member, review);

        if (heart == null) {
            heart = Heart.createHeart(member, review, false);
            heart.like();
        }
        else {
            heart.unlike();
            heartRepository.delete(heart);
        }
        heartRepository.save(heart);
        return review.getLikeCount();
    }
}
