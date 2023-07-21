package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.service.MemberService;
import com.backend.NawaBari.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/api/vi/MyPage")
    public UpdateMyPageResponse UpdateMyPage(@RequestBody MyPageRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        memberService.UpdateMypage(memberId);
        Member member = memberService.findOne(memberId);

        return new UpdateMyPageResponse(member.getId(), member.getProfile_nickname(), member.getProfile_image());
    }

    /**
     * 내가 작성한 리뷰목록
     */
/*    @PostMapping("/api/v1/MyPage/reviews")
    public Slice<ReviewApiController.ReviewDTO> findReview(@RequestBody MyPageRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        reviewService.findMyReview(memberId);
    }*/


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
        private Long id;
        private String profile_nickname;
        private String profile_image;
    }

    @Data
    static class MyPageRequestDTO {
        private Long memberId;
    }
}
