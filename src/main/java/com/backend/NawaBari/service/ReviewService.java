package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.*;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.ReviewDetailDTO;
import com.backend.NawaBari.dto.ReviewUpdateDTO;
import com.backend.NawaBari.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final HeartRepository heartRepository;
    private final PhotoRepository photoRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public Long createReview(Long memberId, Long restaurantId, String title, String content, Double rate, List<Photo> photos) {
        Member member = memberRepository.findOne(memberId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        boolean isAddressMatching = false;
        for (MemberZone memberZone : member.getMemberZones()) {
            if (checkAddress(memberZone.getZone(), restaurant.getAddress_name())) {
                isAddressMatching = true;
                break;
            }
        }

        if (isAddressMatching) { //구 주소가 일치하는 경우 리뷰생성
            Review review = Review.createReview(member, restaurant, title, content, rate, photos);
            restaurant.addReview(review);
            restaurant.updateAverageRating();
            restaurantRepository.save(restaurant);
            reviewRepository.save(review);
            photoRepository.save(photos);

            //식당 메인사진이 없을경우 사진등록
            if (restaurant.getMain_photo_fileName() == null && !photos.isEmpty()) {
                Photo mainPhoto = photos.get(0);
                restaurant.setMain_photo_fileName(mainPhoto.getFile_name());

                String main_image_path = "src/main/resources/static/main_images/";
                String original_image_path = "src/main/resources/static/images/" + mainPhoto.getFile_name();
                String targetPath = main_image_path + mainPhoto.getFile_name();

                try {
                    Files.copy(Paths.get(original_image_path), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return review.getId();
        } else {
            throw new IllegalArgumentException("리뷰를 작성할 수 있는 구역이 아닙니다.");
        }

    }

    //추출한 구 이름과 식당주소가 일치하는지 판별
    private boolean checkAddress(Zone zone, String address_name) {
        String extractedDistrict = zone.getGu();
        return address_name.contains(extractedDistrict);
    }


    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewUpdateDTO updateReview(Long reviewId, Long restaurantId, String title, String content, Double rate, List<Photo> photos) {
        Review review = reviewRepository.findOne(reviewId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);
        review.setPhotos(photos);

        restaurant.setAvgRating(restaurant.getAvgRating());
        restaurant.updateAverageRating();


        return ReviewUpdateDTO.convertToDTO(review);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public Boolean deleteReview(Long reviewId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        Review review = reviewRepository.findOne(reviewId);

        if (review == null) {
            return false;
        }

        for (Photo photo : review.getPhotos()) {
            String filePath = "src/main/resources/static/images/" + photo.getFile_name();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }

        restaurant.removeReview(review);
        restaurant.updateAverageRating();
        reviewRepository.delete(review);

        return true;
    }

    /**
     * 리뷰 전체 조회
     */
    public Slice<Review> findAllReview(Long restaurantId, Pageable pageable) {
        return reviewRepository.findAllReview(restaurantId, pageable);
    }

    /**
     * 리뷰 단건 조회
     */
    public Review findOne(Long id) {
        return reviewRepository.findOne(id);
    }

    /**
     * 특정 회원 리뷰목록 조회
     */
    public Slice<Review> findMyReview(Long memberId, Pageable pageable) {
        return reviewRepository.getReviewsByMember(memberId, pageable);
    }

    /**
     * 리뷰 상세조회
     */
    public ReviewDetailDTO DetailReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);

        Restaurant restaurant = restaurantRepository.findRestaurantByReviewId(reviewId);

        Member writer = memberRepository.findWriterByReviewId(reviewId);

        List<Long> LikeMember = heartRepository.findOneReviewLikeMember(reviewId);

        List<String> photoUrls = new ArrayList<>();
        for (Photo photo : review.getPhotos()) {
            String photoUrl = "/images/" + photo.getFile_name();
            photoUrls.add(photoUrl);
        }

        return new ReviewDetailDTO(restaurant.getId(), restaurant.getAvgRating(),
                writer.getId(), LikeMember,
                review.getTitle(), review.getContent(), review.getRate(),
                review.getLikeCount(), photoUrls, review.getFormattedTime());
    }
}
