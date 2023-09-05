package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.MyPageDTO;
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
        Slice<Review> myReview = reviewService.findMyReview(memberId, pageable);
        List<MyReviewDTO> myReviewDTOs = new ArrayList<>();
        String basePath = "/images/";

        for (Review review : myReview) {
            List<MyReviewPhotoDTO> photoDTOs = review.getPhotos().stream()
                    .map(photo -> new MyReviewPhotoDTO(basePath, photo.getFile_name()))
                    .collect(Collectors.toList());

            MyReviewDTO myReviewDTO = new MyReviewDTO(
                    review.getId(),
                    review.getTitle(),
                    review.getRate(),
                    review.getFormattedTime(),
                    photoDTOs
            );
            myReviewDTOs.add(myReviewDTO);
        }

        return new SliceImpl<>(myReviewDTOs, pageable, myReview.hasNext());
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
    @AllArgsConstructor
    static class MyReviewDTO {
        private Long reviewId;
        private String title;
        private Double rate;
        private String time;
        private List<MyReviewPhotoDTO> photoDTOS;
    }

    @Data
    static class MyReviewPhotoDTO {
        private String photoUrls;

        public MyReviewPhotoDTO(String photoUrls, String basePath) {
            this.photoUrls = photoUrls + basePath;
        }
    }
}
