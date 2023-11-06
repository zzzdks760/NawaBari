package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.repository.BookMarkRepository;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    //북마크 클릭
    @Transactional
    public int addBookMark(Long memberId, Long restaurantId) {
        Member member = memberRepository.findOne(memberId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        BookMark bookMark = bookMarkRepository.findBookMark(memberId, restaurantId);

        if (bookMark == null) {
            bookMark = BookMark.createBookMark(member, restaurant, false);
            bookMark.marked();
        } else {
            bookMark.unMarked();
            bookMarkRepository.delete(bookMark.getId());
        }
        bookMarkRepository.save(bookMark);
        return restaurant.getBookMarkCount();
    }

    /**
     * 회원이 북마크한 식당 조회
     */
    public Slice<RestaurantDTO> findBookMarkRestaurants(Long memberId, Pageable pageable) {
        Slice<Restaurant> bookMarkRestaurants = bookMarkRepository.findBookMarkRestaurants(memberId, pageable);

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        for (Restaurant restaurant : bookMarkRestaurants.getContent()) {

            Review topReview = reviewRepository.findTopReviewTitle(restaurant.getId());

            String topReviewTitle = null;
            if (topReview != null) {
                topReviewTitle = topReview.getTitle();
            }

            RestaurantDTO restaurantDTO = RestaurantDTO.convertToDTO(restaurant);
            restaurantDTO.setTopReviewTitle(topReviewTitle);
            restaurantDTOS.add(restaurantDTO);
        }

        return new SliceImpl<>(restaurantDTOS, bookMarkRestaurants.getPageable(), bookMarkRestaurants.hasNext());
    }
}
