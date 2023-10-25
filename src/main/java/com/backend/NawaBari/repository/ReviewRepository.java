package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    //리뷰 저장
    public void save(Review review) {
        em.persist(review);
    }

    //아이디로 리뷰조회
    public Review findOne(Long id) {
        return em.find(Review.class, id);
    }

    //리뷰 삭제
    @Transactional
    public void delete(Review review) {
        em.remove(review);
    }

    //리뷰 수 조회
    public Long countByReview(Long restaurant_id) {
        return em.createQuery("select count(r) from Review r where r.restaurant.id = :restaurant_id", Long.class)
                .setParameter("restaurant_id", restaurant_id)
                .getSingleResult();
    }

    //리뷰 전체 조회
    public Slice<Review> findAllReview(Long restaurant_id, Pageable pageable) {
        List<Review> reviewList = em.createQuery("select r from Review r left join fetch r.photos where r.restaurant.id = :restaurant_id", Review.class)
                .setParameter("restaurant_id", restaurant_id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(reviewList, pageable, reviewList.size() >= pageable.getPageSize());
    }


    //회원이 작성한 리뷰조회
    public Slice<Review> getReviewsByMember(Long member_id, Pageable pageable) {
        List<Review> reviewList = em.createQuery("select r from Review r left join fetch r.photos where r.writer.id = :member_id", Review.class)
                .setParameter("member_id", member_id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new SliceImpl<>(reviewList, pageable, reviewList.size() >= pageable.getPageSize());
    }


    //가장 좋아요를 많이받은 리뷰 조회
    public Review findTopReviewTitle(Long restaurant_id) {
        try {
            return em.createQuery("select r from Review r left join fetch r.restaurant res where res.id = :restaurant_id ORDER BY r.likeCount DESC", Review.class)
                    .setParameter("restaurant_id", restaurant_id)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 또는 적절한 기본값을 반환하거나 예외를 처리하는 방법을 선택할 수 있습니다.
        }
    }

}

