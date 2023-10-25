package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.BookMark;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class BookMarkRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void save(BookMark bookMark) {
        em.persist(bookMark);
    }

    //DB 에서 북마크 조회
    public BookMark findByMemberIdAndRestaurantId(Long memberId, Long restaurantId) {
        try {
            return em.createQuery("select b from BookMark b where b.memberId = :memberId and b.restaurantId = :restaurantId", BookMark.class)
                    .setParameter("memberId", memberId)
                    .setParameter("restaurantId", restaurantId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void remove(BookMark bookMark) {
        em.remove(bookMark);
    }

    //북마크 삭제
    @Transactional
    public void delete(BookMark bookMark) {
        em.remove(bookMark);
    }
}
