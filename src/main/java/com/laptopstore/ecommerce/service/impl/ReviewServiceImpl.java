package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.product.CustomProductDetailsDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.dto.review.*;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.OrderItemsRepository;
import com.laptopstore.ecommerce.repository.ProductRepository;
import com.laptopstore.ecommerce.repository.ReviewRepository;
import com.laptopstore.ecommerce.repository.UserRepository;
import com.laptopstore.ecommerce.service.ReviewService;
import com.laptopstore.ecommerce.specification.ReviewSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.util.error.BadRequestException;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.RatingUtils.roundToNearestHalf;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemsRepository orderItemsRepository;

    public ReviewServiceImpl(ProductRepository productRepository, UserRepository userRepository, ReviewRepository reviewRepository, OrderItemsRepository orderItemsRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public PageResponse<CustomProductDetailsDto> getProductReviews(String productSlug, String email, ReviewFilterDto reviewFilterDto){
        Product product = this.productRepository.findBySlug(productSlug).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        Double averageRating = this.reviewRepository.findAverageRatingByProduct(product);
        averageRating = (averageRating == null) ? 0D : roundToNearestHalf(averageRating);

        long totalRatings = this.reviewRepository.countByProduct(product);

        Map<Integer, Integer> ratingPercentagesMap = new HashMap<>();
        Map<Integer, Long> ratingCountMap = new HashMap<>();

        for (int i = 0;i<=5;i++){
            long ratingCount = this.reviewRepository.countByProductAndRating(product, i);
            ratingCountMap.put(i, ratingCount);

            Double ratingPercentage = this.reviewRepository.findAverageRatingByProductAndRating(product,i);
            if(ratingPercentage == null){
                ratingPercentagesMap.put(i, 0);
            }else{
                ratingPercentagesMap.put(i,(int) Math.round(ratingPercentage));
            }
        }

        boolean purchased = this.orderItemsRepository.existsByOrderUserAndOrderStatusAndProduct(user, OrderStatusEnum.DELIVERED, product);

        Review myReview = this.reviewRepository.findByUserAndProduct(user, product).orElse(null);

        Specification<Review> specification = Specification
                .where(ReviewSpecifications.equalProductId(product.getId()))
                .and(ReviewSpecifications.notEqualUserId(user.getId()));

        Pageable pageable = PaginationUtils.createPageable(
                reviewFilterDto.getIntegerPage(),
                reviewFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Review.class, reviewFilterDto.getSortBy(), Review.DEFAULT_SORT_FIELD),
                reviewFilterDto.getEnumSortDirection());

        if(reviewFilterDto.getIntegerStarRatings() != null && !reviewFilterDto.getIntegerStarRatings().isEmpty()){
            Specification<Review> currentSpecification = ReviewSpecifications.equalStarRatings(reviewFilterDto.getIntegerStarRatings());
            specification = specification.and(currentSpecification);
        }

        Page<Review> userReviews = this.reviewRepository.findAll(specification, pageable);

        CustomProductDetailsDto customProductDetailsDto = new CustomProductDetailsDto(
                product.getId(),
                product.getProductImages().get(0),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getDiscount(),
                product.getDiscountPrice(),

                myReview,
                purchased,
                totalRatings,
                averageRating,
                ratingPercentagesMap,
                ratingCountMap,
                userReviews.getContent()
        );

        return new PageResponse<>(
                userReviews.getPageable().getPageNumber() + 1,
                userReviews.getTotalPages(),
                userReviews.getTotalElements(),
                customProductDetailsDto
        );
    }

    @Override
    public PageResponse<CustomProductDetailsDto> getProductReviews(String productSlug, ReviewFilterDto reviewFilterDto){
        Product product = this.productRepository.findBySlug(productSlug).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        Double averageRating = this.reviewRepository.findAverageRatingByProduct(product);
        averageRating = (averageRating == null) ? 0D : roundToNearestHalf(averageRating);

        long totalRatings = this.reviewRepository.countByProduct(product);

        Map<Integer, Integer> ratingPercentagesMap = new HashMap<>();
        Map<Integer, Long> ratingCountMap = new HashMap<>();

        for (int i = 0;i<=5;i++){
            long ratingCount = this.reviewRepository.countByProductAndRating(product, i);
            ratingCountMap.put(i, ratingCount);

            Double ratingPercentage = this.reviewRepository.findAverageRatingByProductAndRating(product,i);
            if(ratingPercentage == null){
                ratingPercentagesMap.put(i, 0);
            }else{
                ratingPercentagesMap.put(i,(int) Math.round(ratingPercentage));
            }
        }

        Specification<Review> specification = Specification.where(ReviewSpecifications.equalProductId(product.getId()));

        Pageable pageable = PaginationUtils.createPageable(
                reviewFilterDto.getIntegerPage(),
                reviewFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Review.class, reviewFilterDto.getSortBy(), Review.DEFAULT_SORT_FIELD),
                reviewFilterDto.getEnumSortDirection());

        if(reviewFilterDto.getIntegerStarRatings() != null && !reviewFilterDto.getIntegerStarRatings().isEmpty()){
            Specification<Review> currentSpecification = ReviewSpecifications.equalStarRatings(reviewFilterDto.getIntegerStarRatings());
            specification = specification.and(currentSpecification);
        }

        Page<Review> userReviews = this.reviewRepository.findAll(specification, pageable);

        CustomProductDetailsDto customProductDetailsDto = new CustomProductDetailsDto(
                product.getId(),
                product.getProductImages().get(0),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getDiscount(),
                product.getDiscountPrice(),

                totalRatings,
                averageRating,
                ratingPercentagesMap,
                ratingCountMap,
                userReviews.getContent()
        );

        return new PageResponse<>(
                userReviews.getPageable().getPageNumber() + 1,
                userReviews.getTotalPages(),
                userReviews.getTotalElements(),
                customProductDetailsDto
        );
    }

    @Override
    public Review getUserReviewOnProduct(long reviewId, long productId, String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        Review review = this.reviewRepository.findByIdAndUserAndProduct(reviewId, user, product).orElse(null);
        if(review == null){
            throw new NotFoundException("Không tìm thấy đánh giá");
        }

        return review;
    }

    @Override
    public PageResponse<List<CustomReviewDto>> getReviews(ReviewFilterDto reviewFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                reviewFilterDto.getIntegerPage(),
                reviewFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomReviewDto.VALID_SORT_FIELDS, reviewFilterDto.getSortBy(), CustomReviewDto.DEFAULT_SORT_FIELD),
                reviewFilterDto.getEnumSortDirection()
        );

        Page<CustomReviewDto> reviews = this.reviewRepository.findAllReviews(reviewFilterDto, pageable);

        return new PageResponse<>(
                reviews.getPageable().getPageNumber() + 1,
                reviews.getTotalPages(),
                reviews.getTotalElements(),
                reviews.getContent()
        );
    }

    @Override
    public PageResponse<CustomProductDetailsDto> getProductReviews(long productId, ReviewFilterDto reviewFilterDto) {
        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        Pageable pageable = PaginationUtils.createPageable(
                reviewFilterDto.getIntegerPage(),
                reviewFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomReviewDto.VALID_SORT_FIELDS, reviewFilterDto.getSortBy(), CustomReviewDto.DEFAULT_SORT_FIELD),
                reviewFilterDto.getEnumSortDirection()
        );

        Page<CustomReviewDto> reviews = this.reviewRepository.findAllProductReviews(productId, reviewFilterDto, pageable);

        CustomProductDetailsDto customProductDetailsDto = new CustomProductDetailsDto(
            product.getId(), product.getName(), product.getSlug(), reviews.getContent()
        );

        return new PageResponse<>(
                reviews.getPageable().getPageNumber() + 1,
                reviews.getTotalPages(),
                reviews.getTotalElements(),
                customProductDetailsDto
        );
    }

    @Override
    public Review getReviewDetails(long reviewId) {
        Review review = this.reviewRepository.findById(reviewId).orElse(null);
        if(review == null){
            throw new NotFoundException("Không tìm thấy đánh giá");
        }

        return review;
    }

    @Override
    public void UserCreateReview(long productId, String email, CreateReviewDto createReviewDto){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        boolean isUserBoughtProduct = this.orderItemsRepository.existsByOrderUserAndOrderStatusAndProduct(user, OrderStatusEnum.DELIVERED, product);
        if(!isUserBoughtProduct){
            throw new BadRequestException("Bạn cần phải mua sản phẩm trươc khi để lại đánh giá");
        }

        boolean isUserAlreadyReviewProduct = this.reviewRepository.existsByUserAndProduct(user, product);
        if(isUserAlreadyReviewProduct){
            throw new BadRequestException("Bạn đã đánh giá sản phẩm này rồi");
        }

        Review newReview = new Review(
                user,
                product,
                createReviewDto.getMessage(),
                createReviewDto.getRating()
        );

        this.reviewRepository.save(newReview);
    }

    @Override
    public void UserUpdateReview(long productId, String email, UpdateReviewDto updateReviewDto){
        Review review = this.getUserReviewOnProduct(updateReviewDto.getReviewId(), productId, email);

        review.setRating(updateReviewDto.getRating());
        review.setMessage(updateReviewDto.getMessage());

        this.reviewRepository.save(review);
    }

    @Override
    public void UserDeleteReview(long reviewId, long productId, String email){
        Review review = this.getUserReviewOnProduct(reviewId, productId, email);
        this.reviewRepository.delete(review);
    }

    @Override
    public DeleteReviewDto getInformationForDeleteReview(String email, long productId, long reviewId){
        Review userReview = this.getUserReviewOnProduct(reviewId, productId, email);

        return new DeleteReviewDto(
                userReview.getId(),
                userReview.getProduct().getId(),
                userReview.getProduct().getName()
        );
    }

    @Override
    public CreateReviewDto getInformationForCreateReview(String email, long productId){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }

        boolean isUserBoughtProduct = this.orderItemsRepository.existsByOrderUserAndOrderStatusAndProduct(user, OrderStatusEnum.DELIVERED, product);
        if(!isUserBoughtProduct){
            throw new BadRequestException("Bạn cần phải mua sản phẩm trươc khi để lại đánh giá");
        }

        boolean isUserAlreadyReviewProduct = this.reviewRepository.existsByUserAndProduct(user, product);
        if(isUserAlreadyReviewProduct){
            throw new BadRequestException("Bạn đã đánh giá sản phẩm này rồi");
        }

        return new CreateReviewDto(
                product.getId(),
                product.getName()
        );
    }

    @Override
    public UpdateReviewDto getInformationForUpdateReview(String email, long productId, long reviewId){
        Review userReview = this.getUserReviewOnProduct(reviewId, productId, email);

        return new UpdateReviewDto(
                userReview.getId(),
                userReview.getProduct().getId(),
                userReview.getProduct().getName(),
                userReview.getRating(),
                userReview.getMessage()
        );
    }
}
