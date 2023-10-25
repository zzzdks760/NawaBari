package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.repository.BookMarkRepository;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    //북마크 클릭
    @Transactional
    public int addBookMark(Long memberId, Long restaurantId) {
        BookMark bookMark = bookMarkRepository.findByMemberIdAndRestaurantId(memberId, restaurantId);
        Member member = memberRepository.findOne(memberId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        if (bookMark == null) {
            bookMark = BookMark.createBookMark(member, restaurant, false);
            bookMark.marked();
        } else {
            bookMark.unMarked();
            bookMarkRepository.delete(bookMark);
        }
        return restaurant.getBookMarkCount();
    }
}
