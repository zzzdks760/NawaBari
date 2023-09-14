package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Photo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class PhotoRepository {

    @PersistenceContext
    private final EntityManager em;

    //사진저장
    @Transactional
    public void save(List<Photo> photos) {
        for (Photo photo : photos) {
            em.persist(photo);
        }
    }

    public Photo findOne(Long id) {
        return em.find(Photo.class, id);
    }

    @Transactional
    public void delete(Photo photo) {
        em.remove(photo);
    }
}
