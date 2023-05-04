package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.HeartRepository;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import jakarta.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class HeartServiceTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired HeartService heartService;
    @Autowired MemberRepository memberRepository;
    @Autowired HeartRepository heartRepository;
    @Autowired EntityManager em;

    @Test
    public void 좋아요기능() throws Exception {
        //given
        Member member = Member.builder()
                .profile_nickname("Kim")
                .build();
        em.persist(member);

        Review review = Review.builder()
                .title("맛있음")

                .build();
        em.persist(review);

//        Heart heart = Heart.builder()
//                .member(member)
//                .review(review)
//                .liked(false)
//                .build();
//        em.persist(heart);

        //when
        Boolean aBoolean = heartService.addHeart(member.getId(), review.getId());


        //Then
        assertThat(aBoolean).isTrue();
    }
}