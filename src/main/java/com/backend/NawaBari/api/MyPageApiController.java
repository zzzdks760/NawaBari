package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.domain.review.Review;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageApiController {
    private final MemberService memberService;
    private final ReviewService reviewService;

    /**
     * 마이페이지 조회
     */
    @GetMapping("/api/v1/MyPage")
    public MyPageDTO MyPage(@RequestBody MyPageRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        Member profile = memberService.profile(memberId);

        return new MyPageDTO(profile.getProfile_nickname(), profile.getProfile_image(), profile.getRole());
    }

    /**
     * 마이페이지 수정
     */
    @PatchMapping("/api/v1/MyPage")
    public UpdateMyPageResponse UpdateMyPage(@RequestBody MyPageRequestDTO requestDTO) {
        memberService.UpdateMyPage(requestDTO.getMemberId(), requestDTO.getProfile_nickname(), requestDTO.getProfile_image());

        return new UpdateMyPageResponse(requestDTO.getMemberId(), requestDTO.getProfile_nickname(), requestDTO.getProfile_image());
    }

    /**
     * 내가 작성한 리뷰목록
     */
    @GetMapping("/api/v1/MyPage/reviews")
    public Slice<ReviewApiController.ReviewDTO> findReview(@RequestBody MyPageReviewRequestDTO myPageReviewRequestDTO, @PageableDefault Pageable pageable) {
        Long memberId = myPageReviewRequestDTO.getMemberId();
        Slice<Review> myReview = reviewService.findMyReview(memberId, pageable);
        List<ReviewApiController.ReviewDTO> myReviewDTOs = new ArrayList<>();

        for (Review review : myReview) {
            ReviewApiController.ReviewDTO reviewDTO = new ReviewApiController.ReviewDTO(
                    review.getId(),
//                    review.getPhotos(),
                    review.getTitle(),
                    review.getContent(),
                    review.getRate(),
                    review.getLikeCount()
            );
            myReviewDTOs.add(reviewDTO);
        }

        return new SliceImpl<>(myReviewDTOs, pageable, myReview.hasNext());
    }


    @Data
    @AllArgsConstructor
    static class MyPageDTO {
        private String profile_nickname;
        private String profile_image;
        private Role role;
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

    @Data
    static class MyPageReviewRequestDTO {
        private Long memberId;
    }
}
