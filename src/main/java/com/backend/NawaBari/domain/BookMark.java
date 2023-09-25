package com.backend.NawaBari.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mark_id")
    private Long id;


    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    //==생성 메서드==//
    public static BookMark createBookMark(Long memberId, Long restaurantId) {
        BookMark bookMark = new BookMark();
        bookMark.setMemberId(memberId);
        bookMark.setRestaurantId(restaurantId);
        return bookMark;
    }
}
