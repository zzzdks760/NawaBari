package com.backend.NawaBari.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.NawaBari.domain.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.ReviewDTO;
import com.backend.NawaBari.dto.ReviewDetailDTO;
import com.backend.NawaBari.service.ReviewService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //리뷰등록
    @PostMapping("/api/v1/restaurants/reviews")
    public ReviewResponseDTO createReview(@ModelAttribute ReviewRequestDTO request,
                                          @RequestParam(value = "photos", required = false) List<MultipartFile> photoFiles) {

        if (reviewService.checkAddress(request.getMemberId(), request.getRestaurantId())) {
            List<Photo> photos = new ArrayList<>();

            if (photoFiles != null && !photoFiles.isEmpty()) {
                for (MultipartFile photoFile : photoFiles) {
                    try {
                        String originalFileName = photoFile.getOriginalFilename();
                        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                        String newFileName = UUID.randomUUID() + "." + fileExtension;

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(photoFile.getSize());

                        String webFilePath = "images/" + newFileName;

                        amazonS3.putObject(new PutObjectRequest(bucketName, webFilePath, photoFile.getInputStream(), metadata));
                        String fileUrl = amazonS3.getUrl(bucketName, webFilePath).toString();

                        Photo photo = new Photo();
                        photo.setFile_name(newFileName);
                        photo.setFile_path(fileUrl);
                        photos.add(photo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Long reviewId = reviewService.createReview(request.getMemberId(), request.getRestaurantId(),
                    request.getTitle(), request.getContent(), request.getRate(), photos);

            return new ReviewResponseDTO(reviewId);
        } else {
            throw new IllegalArgumentException("등록할 수 없는 구역입니다.");
        }
    }


    //리뷰수정
    @PatchMapping("/api/v1/restaurants/reviews")
    public Long updateReview(@ModelAttribute UpdateReviewRequestDTO request,
                                        @RequestParam(value = "photos", required = false) List<MultipartFile> photoFiles) {


        List<Photo> photos = reviewService.updatePhoto(request.getReviewId(), request.getRestaurantId(), photoFiles, request.getDeletePhoto());

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
    public Slice<ReviewDTO> RestaurantReviews(@RequestParam("restaurantId")Long restaurantId,
                                              @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
                                              @PageableDefault Pageable pageable) {

        Slice<Review> reviews;
        if ("likes".equalsIgnoreCase(sortBy)) {
            reviews = reviewService.findAllReviewSortByLikes(restaurantId, pageable);
        } else {
            reviews = reviewService.findAllReview(restaurantId, pageable);
        }

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
        private List<Long> deletePhoto;
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
