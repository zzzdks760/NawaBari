package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Member;
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
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    //회원저장
    @Transactional
    public void save(Member member) {
        em.persist(member);
    }

    //아이디로 회원찾기
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    //카카오 아이디로 회원 찾기
    public Optional<Member> findByKakao_Id(String kakao_id) {
        try {
            Member member = em.createQuery("select m from Member m where m.kakao_id = :kakao_id", Member.class)
                    .setParameter("kakao_id", kakao_id)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    //카카오 닉네임으로 회원찾기
    public List<Member> findByName(String profile_nickname) {
        return em.createQuery("select m from Member m where m.profile_nickname = :profile_nickname", Member.class)
                .setParameter("name", profile_nickname)
                .getResultList();
    }

    //회원 전체조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
