package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.ReviewDTO;
import com.backend.NawaBari.dto.ReviewDetailDTO;
import com.backend.NawaBari.dto.ReviewUpdateDTO;
import com.backend.NawaBari.service.ReviewService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    //리뷰등록
    @PostMapping("/api/v1/restaurants/reviews")
    public ReviewResponseDTO createReview(@ModelAttribute ReviewRequestDTO request,
                                          @RequestParam(value = "photos", required = false) List<MultipartFile> photoFiles) {

        List<Photo> photos = new ArrayList<>();

        if (photoFiles != null && !photoFiles.isEmpty()) {
            for (MultipartFile photoFile : photoFiles) {
                try {
                    String originalFileName = photoFile.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    String newFileName = UUID.randomUUID() + "." + fileExtension;

                    String filePath = "src/main/resources/static/images/" + newFileName;

                    try(OutputStream os = new FileOutputStream(new File(filePath))) {
                        os.write(photoFile.getBytes());
                    }

                    String webFilePath = "/images/" + newFileName;

                    Photo photo = new Photo();
                    photo.setFile_name(newFileName);
                    photo.setFile_path(webFilePath);
                    photos.add(photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Long reviewId = reviewService.createReview(request.getMemberId(), request.getRestaurantId(),
                request.getTitle(), request.getContent(), request.getRate(), photos);


        return new ReviewResponseDTO(reviewId);
    }


    //리뷰수정
    @PatchMapping("/api/v1/restaurants/reviews")
    public ReviewUpdateDTO updateReview(@ModelAttribute UpdateReviewRequestDTO request,
                                        @RequestParam(value = "photos", required = false) List<MultipartFile> photoFiles) {

        List<Photo> photos = new ArrayList<>();

        if (photoFiles != null) {
            for (MultipartFile photoFile : photoFiles) {
                try {
                    String originalFileName = photoFile.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    String newFileName = UUID.randomUUID() + "." + fileExtension;

                    String filePath = "src/main/resources/static/images/" + newFileName;

                    try(OutputStream os = new FileOutputStream(new File(filePath))) {
                        os.write(photoFile.getBytes());
                    }

                    String webFilePath = "/images/" + newFileName;

                    Photo photo = new Photo();
                    photo.setFile_name(newFileName);
                    photo.setFile_path(webFilePath);
                    photos.add(photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reviewService.updateReview(request.getReviewId(), request.getRestaurantId(),
                request.getTitle(), request.getContent(), request.getRate(), photos);
    }

    //리뷰삭제
    @DeleteMapping("/api/v1/restaurants/reviews")
    public ResponseEntity<Boolean> deleteReview(@RequestBody DeleteReviewRequestDTO deleteRequest) {
        Long reviewId = deleteRequest.getReviewId();
        Long restaurantId = deleteRequest.getRestaurantId();
        Boolean isDeleted = reviewService.deleteReview(reviewId, restaurantId);

        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //특정 식당 리뷰 전체 조회
    @GetMapping("/api/v1/restaurant/reviews")
    public Slice<ReviewDTO> RestaurantReviews(@RequestParam("restaurantId")Long restaurantId, @PageableDefault Pageable pageable) {

        Slice<Review> reviews = reviewService.findAllReview(restaurantId, pageable);

        List<ReviewDTO> reviewDTOS = reviews.getContent().stream()
                .map(ReviewDTO::convertToDTO)
                .collect(Collectors.toList());
        return new SliceImpl<>(reviewDTOS, reviews.getPageable(), reviews.hasNext());
    }

    //리뷰 상세조회
    @GetMapping("/api/v1/reviews/review")
    public ReviewDetailDTO ReviewDetail(@RequestParam("reviewId") Long reviewId) {
        return reviewService.DetailReview(reviewId);
    }


    //===============================================================================================================//


    @Data
    static class ReviewRequestDTO {
        private Long memberId;
        private Long restaurantId;
        private String title;
        private String content;
        private Double rate;
    }


    @Data
    static class UpdateReviewRequest {
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    static class UpdateReviewRequestDTO {
        private Long reviewId;
        private Long restaurantId;
        private String title;
        private String content;
        private Double rate;
    }

    @Data
    static class DeleteReviewRequestDTO {
        private Long reviewId;
        private Long restaurantId;
    }

    @Data
    static class AllReviewRequestDTO {
        private Long restaurantId;
    }

    @Data
    static class ReviewResponseDTO {
        private Long reviewId;

        public ReviewResponseDTO(Long reviewId) {
            this.reviewId = reviewId;
        }
    }

}
