package com.laptopstore.ecommerce.specification.predicate;

import com.laptopstore.ecommerce.model.Review;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReviewPredicates {
    public static Predicate equalStartRatingsPredicate(Root<Review> root, CriteriaBuilder cb, List<Integer> starRatings){
        List<Predicate> predicates = new ArrayList<>();
        for (int starRating : starRatings) {
            predicates.add(cb.equal(root.get("rating"), starRating));
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate equalProductIdPredicate(Root<Review> root, CriteriaBuilder cb, long productId) {
        return cb.equal(root.get("product").get("id"), productId);
    }

    public static Predicate notEqualUserIdPredicate(Root<Review> root, CriteriaBuilder cb, long userId) {
        return cb.notEqual(root.get("user").get("id"), userId);
    }

    public static Predicate dateBetweenPredicate(Root<Review> root, CriteriaBuilder cb, Instant startDate, Instant endDate) {
        return cb.between(root.get("createdAt"), startDate, endDate);
    }

    public static Predicate emailLikePredicate(Root<Review> root, CriteriaBuilder cb, String email) {
        return cb.like(cb.lower(root.get("user").get("email")), "%" + email.toLowerCase() + "%");
    }
}
