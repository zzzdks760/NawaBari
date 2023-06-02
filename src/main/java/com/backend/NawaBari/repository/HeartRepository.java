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

    public void delete(Heart heart) {
        em.remove(heart);
    }

}
