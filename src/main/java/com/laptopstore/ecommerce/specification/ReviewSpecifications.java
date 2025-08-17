package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.specification.predicate.ReviewPredicates;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecifications {
    public static Specification<Review> equalStarRatings(List<Integer> starRatings){
        return (root, query, criteriaBuilder) -> ReviewPredicates.equalStartRatingsPredicate(root, criteriaBuilder, starRatings);
    }

    public static Specification<Review> equalProductId(long productId){
        return (root, query, criteriaBuilder) -> ReviewPredicates.equalProductIdPredicate(root, criteriaBuilder, productId);
    }

    public static Specification<Review> notEqualUserId(long userId){
        return (root, query, criteriaBuilder) -> ReviewPredicates.notEqualUserIdPredicate(root, criteriaBuilder, userId);
    }

    public static Specification<Review> dateBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> ReviewPredicates.dateBetweenPredicate(root, criteriaBuilder, startDate, endDate);
    }

    public static Specification<Review> emailLike(String email) {
        return (root, query, criteriaBuilder) -> ReviewPredicates.emailLikePredicate(root, criteriaBuilder, email);
    }
}
