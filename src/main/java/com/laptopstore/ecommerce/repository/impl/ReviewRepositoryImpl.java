package com.laptopstore.ecommerce.repository.impl;

import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.dto.review.ReviewFilterDto;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.model.User;
import com.laptopstore.ecommerce.repository.custom.CustomReviewRepository;
import com.laptopstore.ecommerce.specification.predicate.ReviewPredicates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;

public class ReviewRepositoryImpl implements CustomReviewRepository {
    private final EntityManager entityManager;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private List<Predicate> buildPredicates(Root<Review> root, CriteriaBuilder cb, ReviewFilterDto filterDto) {
        List<Predicate> predicates = new ArrayList<>();

        Map<String, Instant> instantRange = getValidInstantRange(
                filterDto.getInstantStartDate(),
                filterDto.getInstantEndDate()
        );

        if(!instantRange.isEmpty()){
            predicates.add(ReviewPredicates.dateBetweenPredicate(root, cb, instantRange.get("startDate"), instantRange.get("endDate")));
        }

        if(filterDto.getIntegerStarRatings() != null && !filterDto.getIntegerStarRatings().isEmpty()){
            predicates.add(ReviewPredicates.equalStartRatingsPredicate(root, cb, filterDto.getIntegerStarRatings()));
        }

        return predicates;
    }

        @Override
        public Page<CustomReviewDto> findAllProductReviews(long productId, ReviewFilterDto reviewFilterDto, Pageable pageable) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // query result
            CriteriaQuery<CustomReviewDto> cq = cb.createQuery(CustomReviewDto.class);
            Root<Review> root = cq.from(Review.class);
            Join<Review, User> userJoin = root.join("user");
            Join<Review, Product> productJoin = root.join("product");

            List<Predicate> predicates = this.buildPredicates(root, cb, reviewFilterDto);
            predicates.add(cb.equal(productJoin.get("id"), productId));

            if(reviewFilterDto.getSearch() != null && !reviewFilterDto.getSearch().isEmpty()){
                predicates.add(cb.like(cb.lower(userJoin.get("email")), "%" + reviewFilterDto.getSearch().toLowerCase() + "%"));
            }

            cq.select(cb.construct(
                            CustomReviewDto.class,
                            root.get("id"),
                            userJoin.get("avatar"),
                            userJoin.get("email"),
                            root.get("rating"),
                            root.get("createdAt"),
                            root.get("updatedAt")
                    ))
                    .where(cb.and(predicates.toArray(new Predicate[0])));

            // sort
            List<Order> orders = new ArrayList<>();
            for (Sort.Order sortOrder : pageable.getSort()) {
                if ("userEmail".equals(sortOrder.getProperty())) {
                    Order order = sortOrder.isAscending() ? cb.asc(userJoin.get("email")) : cb.desc(userJoin.get("email"));
                    orders.add(order);
                }else{
                    Path<?> sortPath = root.get(sortOrder.getProperty());
                    Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
                    orders.add(order);
                }
            }

            cq.orderBy(orders);

            TypedQuery<CustomReviewDto> query = entityManager.createQuery(cq);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<CustomReviewDto> results = query.getResultList();

            // count total
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Review> countRoot = countQuery.from(Review.class);
            Join<Review, User> countUserJoin = countRoot.join("user");
            Join<Review, Product> countProductJoin = countRoot.join("product");

            List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, reviewFilterDto);
            countPredicates.add(cb.equal(countProductJoin.get("id"), productId));

            if(reviewFilterDto.getSearch() != null && !reviewFilterDto.getSearch().isEmpty()){
                countPredicates.add(cb.like(cb.lower(countUserJoin.get("email")), "%" + reviewFilterDto.getSearch().toLowerCase() + "%"));
            }

            countQuery.select(cb.countDistinct(countRoot));
            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

            Long total = entityManager.createQuery(countQuery).getSingleResult();

            return new PageImpl<>(results, pageable, total);
        }

    @Override
    public Page<CustomReviewDto> findAllReviews(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomReviewDto> cq = cb.createQuery(CustomReviewDto.class);
        Root<Review> root = cq.from(Review.class);
        Join<Review, User> userJoin = root.join("user");
        Join<Review, Product> productJoin = root.join("product");

        cq.select(cb.construct(
                        CustomReviewDto.class,
                        root.get("id"),
                        productJoin.get("id"),
                        productJoin.get("name"),
                        userJoin.get("avatar"),
                        userJoin.get("email"),
                        root.get("rating"),
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<CustomReviewDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomReviewDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Review> countRoot = countQuery.from(Review.class);
        Join<Review, User> countUserJoin = countRoot.join("user");
        Join<Review, Product> countProductJoin = countRoot.join("product");

        countQuery.select(cb.countDistinct(countRoot));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomReviewDto> findAllReviews(ReviewFilterDto reviewFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomReviewDto> cq = cb.createQuery(CustomReviewDto.class);
        Root<Review> root = cq.from(Review.class);
        Join<Review, User> userJoin = root.join("user");
        Join<Review, Product> productJoin = root.join("product");

        List<Predicate> predicates = this.buildPredicates(root, cb, reviewFilterDto);

        if(reviewFilterDto.getSearch() != null && !reviewFilterDto.getSearch().isEmpty()){
            predicates.add(cb.like(cb.lower(userJoin.get("email")), "%" + reviewFilterDto.getSearch().toLowerCase() + "%"));
        }

        cq.select(cb.construct(
                        CustomReviewDto.class,
                        root.get("id"),
                        productJoin.get("id"),
                        productJoin.get("name"),
                        userJoin.get("avatar"),
                        userJoin.get("email"),
                        root.get("rating"),
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        // sort
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            if ("userEmail".equals(sortOrder.getProperty())) {
                Order order = sortOrder.isAscending() ? cb.asc(userJoin.get("email")) : cb.desc(userJoin.get("email"));
                orders.add(order);
            }else if("productName".equals(sortOrder.getProperty())){
                Order order = sortOrder.isAscending() ? cb.asc(productJoin.get("name")) : cb.desc(productJoin.get("name"));
                orders.add(order);
            }else{
                Path<?> sortPath = root.get(sortOrder.getProperty());
                Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
                orders.add(order);
            }
        }

        cq.orderBy(orders);

        TypedQuery<CustomReviewDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomReviewDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Review> countRoot = countQuery.from(Review.class);
        Join<Review, User> countUserJoin = countRoot.join("user");
        Join<Review, Product> countProductJoin = countRoot.join("product");

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, reviewFilterDto);

        if(reviewFilterDto.getSearch() != null && !reviewFilterDto.getSearch().isEmpty()){
            countPredicates.add(cb.like(cb.lower(countUserJoin.get("email")), "%" + reviewFilterDto.getSearch().toLowerCase() + "%"));
        }

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
}
