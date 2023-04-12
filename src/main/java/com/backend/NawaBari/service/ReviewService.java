package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.ReviewDTO;
import com.backend.NawaBari.exception.InvalidZoneException;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public void createReview(ReviewDTO reviewDTO) {
        Member member = memberRepository.findOne(reviewDTO.getWriter().getId());

        if (!member.getMemberZones().equals(reviewDTO.getRestaurant().getZone())){
            throw new InvalidZoneException("회원이 설정한 구역이 아닙니다");
        }

        Review review = Review.toReviewSaveEntity(reviewDTO);
        reviewRepository.save(review);
    }


}
