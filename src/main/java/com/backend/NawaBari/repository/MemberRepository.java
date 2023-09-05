package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.SocialType;
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
    public Member findByKakao_Id(String kakao_id) {
        try {
            return em.createQuery("select m from Member m where m.kakao_id = :kakao_id", Member.class)
                    .setParameter("kakao_id", kakao_id)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }

    }

    //회원 전체조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //이메일로 회원찾기
    public Optional<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();
    }

    //리프레시토큰으로 회원찾기
    public Optional<Member> findByRefreshToken(String refreshToken) {
        return em.createQuery("select m from Member m where m.refreshToken = :refreshToken", Member.class)
                .setParameter("refreshToken", refreshToken)
                .getResultStream().findFirst();
    }

    @Transactional
    public void saveAndFlush(Member member) {
        em.merge(member);
    }

    @Transactional
    public Member memberSave(Member createdUser) {
        em.persist(createdUser);
        return createdUser;
    }

    @Transactional
    public void saveRefreshToken(String email, String refreshToken) {
        Member member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
        member.setRefreshToken(refreshToken);
        em.persist(member);
    }

    //회원아이디로 회원의 설정한 구역의 동이름 조회
    public List<String> findMemberIdByDongName(Long id) {
        return em.createQuery("select mz.zone.dong from MemberZone mz " +
                "where mz.member.id = :id", String.class)
                .setParameter("id", id)
                .getResultList();
    }

    //회원아이디로 회원이 설정한 구역아이디 반환
    public List<Long> findZoneId(Long id) {
        return em.createQuery("select mz.zone.id from MemberZone mz " +
                "where mz.member.id = :id", Long.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Member findWriterByReviewId(Long reviewId) {
        return em.createQuery("select m from Member m join fetch m.reviews rev where rev.id = :reviewId", Member.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }
}
