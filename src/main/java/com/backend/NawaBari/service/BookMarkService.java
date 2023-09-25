package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;


    @Transactional
    public Long addBookMark(Long memberId, Long restaurantId) {
        BookMark findBookMark = bookMarkRepository.findByMemberIdAndRestaurantId(memberId, restaurantId);

        //북마크 취소
        if (findBookMark != null) {
            bookMarkRepository.remove(findBookMark);
        } else {
            BookMark bookMark = BookMark.createBookMark(memberId, restaurantId);
            bookMarkRepository.save(bookMark);
        }
        return memberId;
    }
}
