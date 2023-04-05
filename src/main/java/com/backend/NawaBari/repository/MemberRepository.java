package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Member findOne(Long member_id) {
        return em.find(Member.class, member_id);
    }

    //카카오 아이디로 회원 찾기
    public Member findByKakao_Id(String kakao_id) {
        return em.createQuery("select m from Member m where m.kakao_id = :kakao_id", Member.class)
                .setParameter("kakao_id", kakao_id)
                .getSingleResult();
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
