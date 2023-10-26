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
    public BookMark findBookMark(Long memberId, Long restaurantId) {

        return em.createQuery("select b from BookMark b where b.member.id = :memberId and b.restaurant.id = :restaurantId", BookMark.class)
                .setParameter("memberId", memberId)
                .setParameter("restaurantId", restaurantId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

    }

    //북마크 삭제
    @Transactional
    public void delete(Long bookMarkId) {
        em.createQuery("delete from BookMark where id = :bookMarkId")
                .setParameter("bookMarkId", bookMarkId)
                .executeUpdate();
    }
}
