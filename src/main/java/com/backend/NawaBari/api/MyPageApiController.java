package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.MyPageDTO;
import com.backend.NawaBari.dto.MyReviewDTO;
import com.backend.NawaBari.service.MemberService;
import com.backend.NawaBari.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MyPageApiController {
    private final MemberService memberService;
    private final ReviewService reviewService;

    /**
     * 마이페이지 조회
     */
    @GetMapping("/api/v1/my-page")
    public MyPageDTO MyPage(@RequestParam("id") Long memberId) {
        return memberService.getProfile(memberId);
    }

    /**
     * 마이페이지 수정
     */
    @PatchMapping("/api/v1/my-page")
    public UpdateMyPageResponse UpdateMyPage(@RequestBody MyPageRequestDTO requestDTO) {
        memberService.UpdateMyPage(requestDTO.getMemberId(), requestDTO.getProfile_nickname(), requestDTO.getProfile_image());

        return new UpdateMyPageResponse(requestDTO.getMemberId(), requestDTO.getProfile_nickname(), requestDTO.getProfile_image());
    }

    /**
     * 내가 작성한 리뷰목록
     */
    @GetMapping("/api/v1/my-page/reviews")
    public Slice<MyReviewDTO> findReview(@RequestParam("memberId")Long memberId, @PageableDefault Pageable pageable) {
        //내가 작성한 리뷰의 식당 객체 가져오기 (메인사진, 상호명), 리뷰는 좋아요 수
        return reviewService.findMyReviews(memberId, pageable);
    }

    @Data
    @AllArgsConstructor
    static class UpdateMyPageResponse {
        private Long memberId;
        private String profile_nickname;
        private String profile_image;
    }

    @Data
    static class MyPageRequestDTO {
        private Long memberId;
        private String profile_nickname;
        private String profile_image;
    }
}
