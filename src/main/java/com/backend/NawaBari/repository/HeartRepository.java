package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class HeartRepository {

    @PersistenceContext
    private final EntityManager em;


    public void save(Heart heart) {
        em.persist(heart);
    }

    public Heart findOne(Long id) {
        return em.find(Heart.class, id);
    }

    public Heart findLiked(Member member, Review review) {
            return em.createQuery("select h from Heart h where h.member = :member and h.review = :review", Heart.class)
                    .setParameter("member", member)
                    .setParameter("review", review)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }

    public void delete(Long heartId) {
        em.createQuery("delete from Heart where id = :heartId")
                .setParameter("heartId", heartId)
                .executeUpdate();
    }

    //회원아이디 리뷰아이디로 좋아요 아이디 조회
    public Heart findIdByMemberIdAndReviewId(Long memberId, Long reviewId) {
        return em.createQuery("select h from Heart h where h.member.id = :memberId and h.review.id = :reviewId", Heart.class)
                .setParameter("memberId", memberId)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }

    public List<Long> findOneReviewLikeMember(Long reviewId) {
        return em.createQuery("select h.member.id from Heart h where h.review.id = :reviewId", Long.class)
                .setParameter("reviewId", reviewId)
                .getResultList();
    }
}
