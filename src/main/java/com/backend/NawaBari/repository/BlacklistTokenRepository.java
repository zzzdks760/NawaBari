package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.BlacklistToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class BlacklistTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(BlacklistToken blacklistToken) {
        em.persist(blacklistToken);
    }

    public boolean existsByToken(String token) {
        return em.createQuery("select count(b) > 0 from BlacklistToken b where b.token = :token", Boolean.class)
                .setParameter("token", token)
                .getSingleResult();
    }
}
