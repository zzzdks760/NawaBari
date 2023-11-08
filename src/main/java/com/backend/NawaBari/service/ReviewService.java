package com.backend.NawaBari.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.backend.NawaBari.domain.*;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.MyReviewDTO;
import com.backend.NawaBari.dto.PhotoDTO;
import com.backend.NawaBari.dto.PhotoInfo;
import com.backend.NawaBari.dto.ReviewDetailDTO;
import com.backend.NawaBari.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final HeartRepository heartRepository;
    private final PhotoRepository photoRepository;

    private final AmazonS3 amazonS3;
    /**
     * 리뷰 생성
     */
    @Transactional
    public Long createReview(Long memberId, Long restaurantId, String title, String content, Double rate, List<Photo> photos) {
        Member member = memberRepository.findOne(memberId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);


        Review review = Review.createReview(member, restaurant, title, content, rate, photos);
        restaurant.addReview(review);
        restaurant.updateAverageRating();

        restaurantRepository.save(restaurant);
        reviewRepository.save(review);
        photoRepository.save(photos);

        return review.getId();
    }

    //추출한 구 이름과 식당주소가 일치하는지 판별
    public boolean checkAddress(Long member_id, Long restaurant_id) {
        Member member = memberRepository.findOne(member_id);
        Restaurant restaurant = restaurantRepository.findOne(restaurant_id);

        boolean isAddressMatching = false;

        for (MemberZone memberZone : member.getMemberZones()) {
            String extractedDistrict = memberZone.getZone().getGu();

            if (restaurant.getAddress_name().contains(extractedDistrict)) {
                isAddressMatching = true;
                break; // 일치하는 구역이 하나라도 있으면 반복문 종료
            }
        }

        if (!isAddressMatching) {
            throw new IllegalArgumentException("등록할 수 없는 구역입니다.");
        }

        return true;
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public Long updateReview(Long reviewId, Long restaurantId, String title, String content, Double rate, List<Photo> photos) {
        Review review = reviewRepository.findOne(reviewId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);

        for (Photo photo : photos) {
            photo.setReview(review);
        }

        restaurant.setAvgRating(restaurant.getAvgRating());
        restaurant.updateAverageRating();

        photoRepository.save(photos);
        restaurantRepository.save(restaurant);
        reviewRepository.save(review);



        return review.getId();
    }

    @Transactional
    public List<Photo> updatePhoto(Long reviewId, Long restaurantId, List<MultipartFile> photoFiles, List<Long> deletePhoto) {
        Review review = reviewRepository.findOne(reviewId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);


        //삭제된 사진 아이디가 담겨온 경우
        if (deletePhoto != null) {
            for (Long id : deletePhoto) {
                //DB와 S3에서 사진 삭제
                Photo photo = photoRepository.findOne(id);
                if (photo != null) {
                    deletePhoto(photo);
                    photoRepository.delete(photo);
                }
            }
        }

        List<Photo> uploadedPhotos = new ArrayList<>();

        //사진이 추가 되었을 경우
        if (photoFiles != null) {
            uploadedPhotos = addPhoto(photoFiles);
        }

        return uploadedPhotos;
    }

    @Transactional
    private void deletePhoto(Photo photo) {

        amazonS3.deleteObject(bucketName, "images/" + photo.getFile_name());
    }

    @Transactional
    private List<Photo> addPhoto(List<MultipartFile> photoFiles) {
        List<Photo> uploadedPhotos = new ArrayList<>();

        for (MultipartFile photoFile : photoFiles) {
            try {
                String originalFileName = photoFile.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String newFileName = UUID.randomUUID() + "." + fileExtension;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(photoFile.getSize());

                String webFilePath = "images/" + newFileName;

                // S3에 파일 업로드
                amazonS3.putObject(new PutObjectRequest(bucketName, webFilePath, photoFile.getInputStream(), metadata));

                // 업로드된 파일의 URL 얻기
                String fileUrl = amazonS3.getUrl(bucketName, webFilePath).toString();

                // Photo 객체 생성 및 정보 설정
                Photo photo = new Photo();
                photo.setFile_name(newFileName);
                photo.setFile_path(fileUrl);

                // 업로드된 사진 리스트에 추가
                uploadedPhotos.add(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadedPhotos;
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

        //s3에서 삭제
        for (Photo photo : review.getPhotos()) {
            amazonS3.deleteObject(bucketName, "images/" + photo.getFile_name());
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
    public Slice<MyReviewDTO> findMyReviews(Long memberId, Pageable pageable) {
        Slice<Review> reviews = reviewRepository.getReviewsByMember(memberId, pageable);

        List<MyReviewDTO> myReviewDTOS = reviews.getContent().stream()
                .map(review -> {
                    MyReviewDTO myReviewDTO = MyReviewDTO.convertToDTO(review);

                    //리뷰의 사진 정보 존재하면 하나만 가져오기
                    List<Photo> photos = photoRepository.findPhotoByReviewId(review.getId());
                    if (!photos.isEmpty()) {
                        PhotoDTO photoDTO = new PhotoDTO(photos.get(0).getId(), photos.get(0).getFile_path());
                        myReviewDTO.setPhotoDTOS(Collections.singletonList(photoDTO));
                    }

                    return myReviewDTO;
                })
                .collect(Collectors.toList());
        return new SliceImpl<>(myReviewDTOS, reviews.getPageable(), reviews.hasNext());
    }

    /**
     * 리뷰 상세조회
     */
    public ReviewDetailDTO DetailReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);

        Restaurant restaurant = restaurantRepository.findRestaurantByReviewId(reviewId);

        Member writer = memberRepository.findWriterByReviewId(reviewId);

        List<Long> LikeMember = heartRepository.findOneReviewLikeMember(reviewId);

        List<PhotoInfo> photoInfos = new ArrayList<>();
        if (review.getPhotos() != null) {
            for (Photo photo : review.getPhotos()) {
                PhotoInfo photoInfo = new PhotoInfo(photo.getId(), photo.getFile_path());
                photoInfos.add(photoInfo);
            }
        }

        return new ReviewDetailDTO(
                restaurant.getId(),
                restaurant.getAvgRating(),
                writer.getId(),
                LikeMember,
                review.getTitle(),
                review.getContent(),
                review.getRate(),
                review.getLikeCount(),
                photoInfos,
                review.getFormattedTime()
        );
    }
}
