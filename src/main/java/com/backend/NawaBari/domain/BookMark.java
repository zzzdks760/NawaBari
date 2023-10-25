package com.backend.NawaBari.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mark_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Boolean marked;

    //==생성 메서드==//
    public static BookMark createBookMark(Member member, Restaurant restaurant, Boolean marked) {
        BookMark bookMark = new BookMark();
        bookMark.setMember(member);
        bookMark.setRestaurant(restaurant);
        bookMark.setMarked(false);
        return bookMark;
    }

    public void marked() {
        if (!this.marked){
            this.marked = true;
            this.restaurant.addBookMark(this);
        } else {
            this.unMarked();
        }
    }

    public void unMarked() {
        if (this.marked) {
            this.marked = false;
            this.restaurant.removeBookMark(this);
        }
    }

}
