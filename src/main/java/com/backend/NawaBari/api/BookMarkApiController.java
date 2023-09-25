package com.backend.NawaBari.api;

import com.backend.NawaBari.service.BookMarkService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookMarkApiController {
    private final BookMarkService bookMarkService;

    @PostMapping("/api/v1/restaurant/book-mark")
    public Long bookMark(@RequestBody BookMarkRequest bookMarkRequest) {
        Long memberId = bookMarkRequest.getMemberId();
        Long restaurantId = bookMarkRequest.getRestaurantId();
        return bookMarkService.addBookMark(memberId, restaurantId);
    }

    @Getter
    static class BookMarkRequest {
        private Long memberId;
        private Long restaurantId;
    }
}
