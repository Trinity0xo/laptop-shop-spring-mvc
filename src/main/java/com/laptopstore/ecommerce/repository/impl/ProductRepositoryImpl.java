package com.laptopstore.ecommerce.repository.impl;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.model.OrderItem;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.model.Review;
import com.laptopstore.ecommerce.repository.custom.CustomProductRepository;
import com.laptopstore.ecommerce.specification.predicate.ProductPredicates;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
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
import static com.laptopstore.ecommerce.util.PriceUtils.getValidPriceRange;

public class ProductRepositoryImpl implements CustomProductRepository {
    private final EntityManager entityManager;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private List<Predicate> buildPredicates(Root<Product> root, CriteriaBuilder cb, ProductFilterDto filterDto) {
        List<Predicate> predicates = new ArrayList<>();

        if (filterDto.getSearch() != null && !filterDto.getSearch().isEmpty()) {
            predicates.add(ProductPredicates.nameLikePredicate(root, cb, filterDto.getSearch()));
        }

        if (filterDto.getCategories() != null && !filterDto.getCategories().isEmpty()) {
            predicates.add(ProductPredicates.categoryInPredicate(root, cb, filterDto.getCategories()));
        }

        if (filterDto.getBrands() != null && !filterDto.getBrands().isEmpty()) {
            predicates.add(ProductPredicates.brandInPredicate(root, cb, filterDto.getBrands()));
        }

        if (filterDto.getCpuBrands() != null && !filterDto.getCpuBrands().isEmpty()) {
            predicates.add(ProductPredicates.likeCpuBrandsPredicate(root, cb, filterDto.getCpuBrands()));
        }

        if (filterDto.getGpuBrands() != null && !filterDto.getGpuBrands().isEmpty()) {
            predicates.add(ProductPredicates.likeGpuBrandsPredicate(root, cb, filterDto.getGpuBrands()));
        }

        if (filterDto.getRamSizes() != null && !filterDto.getRamSizes().isEmpty()) {
            predicates.add(ProductPredicates.likeRamSizesPredicate(root, cb, filterDto.getRamSizes()));
        }

        if (filterDto.getStorageSizes() != null && !filterDto.getStorageSizes().isEmpty()) {
            predicates.add(ProductPredicates.likeStorageSizesPredicate(root, cb, filterDto.getStorageSizes()));
        }

        Map<String, Double> priceRange = getValidPriceRange(
                filterDto.getDoubleMinPrice(),
                filterDto.getDoubleMaxPrice(),
                ProductFilterDto.MIN_PRICE,
                ProductFilterDto.MAX_PRICE
        );
        if (!priceRange.isEmpty()) {
            predicates.add(ProductPredicates.priceBetweenPredicate(root, cb, priceRange.get("min"), priceRange.get("max")));
        }

        Map<String, Double> discountPriceRange = getValidPriceRange(
                filterDto.getDoubleMinDiscountPrice(),
                filterDto.getDoubleMaxDiscountPrice(),
                ProductFilterDto.MIN_PRICE,
                ProductFilterDto.MAX_PRICE
        );
        if (!discountPriceRange.isEmpty()) {
            predicates.add(ProductPredicates.discountPriceBetweenPredicate(root, cb, discountPriceRange.get("min"), discountPriceRange.get("max")));
        }

        return predicates;
    }

    @Override
    public Page<CustomProductDto> findShopProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductDto> cq = cb.createQuery(CustomProductDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);

        // calculate discount price
        Expression<Double> price = cb.coalesce(root.get("price"), 0.0);
        Expression<Double> discount = cb.coalesce(root.get("discount"), 0.0);

        // price * discount
        Expression<Double> priceTimesDiscount = cb.prod(price, discount);

        // (price * discount) / 100
        Expression<Number> discountAmountRaw = cb.quot(priceTimesDiscount, cb.literal(100.0));
        Expression<Double> discountAmount = discountAmountRaw.as(Double.class);

        // price - ((price * discount) / 100)
        Expression<Double> discountPrice = cb.diff(price, discountAmount);


        cq.select(cb.construct(
                CustomProductDto.class,
                root.get("id"),
                imageJoin,
                root.get("name"),
                root.get("slug"),
                cb.coalesce(cb.avg(reviewJoin.get("rating")), 0),
                root.get("price"),
                root.get("discount"),
                discountPrice,
                root.get("createdAt"),
                root.get("updatedAt")
        ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), root.get("slug"), root.get("price"), imageJoin, root.get("updatedAt"), root.get("createdAt"));

        // sort
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            if ("discountPrice".equals(sortOrder.getProperty())) {
                Order order = sortOrder.isAscending() ? cb.asc(discountPrice) : cb.desc(discountPrice);
                orders.add(order);
            } else if("averageRating".equals(sortOrder.getProperty())){
                Order order = sortOrder.isAscending() ? cb.asc(cb.coalesce(cb.avg(reviewJoin.get("rating")), 0)) : cb.desc(cb.coalesce(cb.avg(reviewJoin.get("rating")), 0));
                orders.add(order);
            }else {
                Path<?> sortPath = root.get(sortOrder.getProperty());
                Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
                orders.add(order);
            }
        }

        cq.orderBy(orders);

        TypedQuery<CustomProductDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductDto> findAllProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductDto> cq = cb.createQuery(CustomProductDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
        if (productFilterDto.getEnumStockStatus() != null) {
            predicates.add(ProductPredicates.stockStatusPredicate(root, cb, productFilterDto.getEnumStockStatus()));
        }

        cq.select(cb.construct(
                CustomProductDto.class,
                root.get("id"),
                imageJoin,
                root.get("name"),
                root.get("slug"),
                root.get("quantity"),
                root.get("createdAt"),
                root.get("updatedAt")
        ))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        // sort
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            Path<?> sortPath = root.get(sortOrder.getProperty());
            Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
            orders.add(order);
        }
        cq.orderBy(orders);

        TypedQuery<CustomProductDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);
        if (productFilterDto.getEnumStockStatus() != null) {
            countPredicates.add(ProductPredicates.stockStatusPredicate(countRoot, cb, productFilterDto.getEnumStockStatus()));
        }

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductSoldDto> findTopSellingProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductSoldDto> cq = cb.createQuery(CustomProductSoldDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, OrderItem> orderItemJoin = root.join("orderItems");
        Join<OrderItem, Order> orderJoin = orderItemJoin.join("order");

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
        predicates.add(cb.equal(orderJoin.get("status"), OrderStatusEnum.DELIVERED));

        Map<String, Instant> instantRange = getValidInstantRange(productFilterDto.getInstantStartDate(), productFilterDto.getInstantEndDate());

        if(!instantRange.isEmpty()){
            predicates.add(cb.between(orderJoin.get("createdAt"), instantRange.get("startDate"), instantRange.get("endDate")));
        }

        cq.select(cb.construct(
                        CustomProductSoldDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.sum(orderItemJoin.get("quantity")),
                        root.get("quantity")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), root.get("quantity"), imageJoin)
                .orderBy(cb.desc(cb.sum(orderItemJoin.get("quantity"))));

        TypedQuery<CustomProductSoldDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductSoldDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        Join<Product, OrderItem> countOrderItemJoin = countRoot.join("orderItems");
        Join<OrderItem, Order> countOrderJoin = countOrderItemJoin.join("order");

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);
        countPredicates.add(cb.equal(countOrderJoin.get("status"), OrderStatusEnum.DELIVERED));

        if(!instantRange.isEmpty()){
            countPredicates.add(cb.between(countOrderJoin.get("createdAt"), instantRange.get("startDate"), instantRange.get("endDate")));
        }

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductSoldDto> findTopSellingProducts(Instant time, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductSoldDto> cq = cb.createQuery(CustomProductSoldDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, OrderItem> orderItemJoin = root.join("orderItems");
        Join<OrderItem, Order> orderJoin = orderItemJoin.join("order");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(orderJoin.get("status"), OrderStatusEnum.DELIVERED));
        predicates.add(cb.greaterThanOrEqualTo(orderJoin.get("createdAt"), time));

        cq.select(cb.construct(
                        CustomProductSoldDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.sum(orderItemJoin.get("quantity")),
                        root.get("quantity")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), root.get("quantity"), imageJoin)
                .orderBy(cb.desc(cb.sum(orderItemJoin.get("quantity"))));

        TypedQuery<CustomProductSoldDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductSoldDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        Join<Product, OrderItem> countOrderItemJoin = countRoot.join("orderItems");
        Join<OrderItem, Order> countOrderJoin = countOrderItemJoin.join("order");

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countOrderJoin.get("status"), OrderStatusEnum.DELIVERED));
        countPredicates.add(cb.greaterThanOrEqualTo(countOrderJoin.get("createdAt"), time));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }


    @Override
    public Page<CustomProductRatingDto> findTopRatedProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductRatingDto> cq = cb.createQuery(CustomProductRatingDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, Review> reviewJoin = root.join("reviews");

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);

        Map<String, Instant> dateRange = getValidInstantRange(productFilterDto.getInstantStartDate(), productFilterDto.getInstantEndDate());

        if(!dateRange.isEmpty()){
            predicates.add(cb.between(reviewJoin.get("createdAt"), dateRange.get("startDate"), dateRange.get("endDate")));
        }

        cq.select(cb.construct(
                        CustomProductRatingDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.avg(reviewJoin.get("rating")),
                        cb.count(reviewJoin.get("id"))
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), imageJoin)
                .orderBy(cb.desc(cb.avg(reviewJoin.get("rating"))), cb.desc(cb.count(reviewJoin.get("id"))))
                .having(cb.ge(cb.avg(reviewJoin.get("rating")), 4));


        TypedQuery<CustomProductRatingDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductRatingDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> productRoot = countQuery.from(Product.class);

        Subquery<Long> subquery = countQuery.subquery(Long.class);
        Root<Product> subProduct = subquery.from(Product.class);
        Join<Product, Review> countReviewJoin = subProduct.join("reviews");

        List<Predicate> countPredicates = this.buildPredicates(subProduct, cb, productFilterDto);
        if(!dateRange.isEmpty()){
            countPredicates.add(cb.between(countReviewJoin.get("createdAt"), dateRange.get("startDate"), dateRange.get("endDate")));
        }

        subquery.select(subProduct.get("id"))
                .where(cb.and(countPredicates.toArray(new Predicate[0])))
                .groupBy(subProduct.get("id"))
                .having(cb.ge(cb.avg(countReviewJoin.get("rating")), 4));

        countQuery.select(cb.count(cb.literal(1)))
                .where(productRoot.get("id").in(subquery));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductRatingDto> findTopRatedProducts(Instant time, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductRatingDto> cq = cb.createQuery(CustomProductRatingDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, Review> reviewJoin = root.join("reviews");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThanOrEqualTo(reviewJoin.get("createdAt"), time));

        cq.select(cb.construct(
                        CustomProductRatingDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.avg(reviewJoin.get("rating")),
                        cb.count(reviewJoin.get("id"))
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), imageJoin)
                .orderBy(cb.desc(cb.avg(reviewJoin.get("rating"))), cb.desc(cb.count(reviewJoin.get("id"))))
                .having(cb.ge(cb.avg(reviewJoin.get("rating")), 4));

        TypedQuery<CustomProductRatingDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductRatingDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> productRoot = countQuery.from(Product.class);

        Subquery<Long> subquery = countQuery.subquery(Long.class);
        Root<Product> subProduct = subquery.from(Product.class);
        Join<Product, Review> countReviewJoin = subProduct.join("reviews");

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.greaterThanOrEqualTo(countReviewJoin.get("createdAt"), time));

        subquery.select(subProduct.get("id"))
                .where(cb.and(countPredicates.toArray(new Predicate[0])))
                .groupBy(subProduct.get("id"))
                .having(cb.ge(cb.avg(countReviewJoin.get("rating")), 4));

        countQuery.select(cb.count(cb.literal(1)))
                .where(productRoot.get("id").in(subquery));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

//    @Override
//    public Page<CustomProductDto> findTopRatedProducts(Instant time, ProductFilterDto productFilterDto, Pageable pageable) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//
//        // query result
//        CriteriaQuery<CustomProductDto> cq = cb.createQuery(CustomProductDto.class);
//        Root<Product> root = cq.from(Product.class);
//
//        Join<Product, ProductImage> imageJoin = root.join("productImages");
//        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
//        Join<Product, Review> reviewJoin = root.join("reviews");
//
//        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
//        predicates.add(cb.greaterThanOrEqualTo(reviewJoin.get("createdAt"), time));
//
//
//        cq.select(cb.construct(
//                        CustomProductDto.class,
//                        root.get("id"),
//                        imageJoin,
//                        root.get("name"),
//                        cb.avg(reviewJoin.get("rating")),
//                        cb.count(reviewJoin.get("id"))
//                ))
//                .where(cb.and(predicates.toArray(new Predicate[0])))
//                .groupBy(root.get("id"), root.get("name"), imageJoin)
//                .orderBy(cb.desc(cb.avg(reviewJoin.get("rating"))), cb.desc(cb.count(reviewJoin.get("id"))))
//                .having(cb.ge(cb.avg(reviewJoin.get("rating")), 4));
//
//        TypedQuery<CustomProductDto> query = entityManager.createQuery(cq);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<CustomProductDto> results = query.getResultList();
//
//        // count total
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Root<Product> productRoot = countQuery.from(Product.class);
//
//        Subquery<Long> subquery = countQuery.subquery(Long.class);
//        Root<Product> subProduct = subquery.from(Product.class);
//        Join<Product, Review> countReviewJoin = subProduct.join("reviews");
//
//        List<Predicate> countPredicates = this.buildPredicates(subProduct, cb, productFilterDto);
//        countPredicates.add(cb.greaterThanOrEqualTo(countReviewJoin.get("createdAt"), time));
//
//        subquery.select(subProduct.get("id"))
//                .where(cb.and(countPredicates.toArray(new Predicate[0])))
//                .groupBy(subProduct.get("id"))
//                .having(cb.ge(cb.avg(countReviewJoin.get("rating")), 4));
//
//        countQuery.select(cb.count(cb.literal(1)))
//                .where(productRoot.get("id").in(subquery));
//
//        Long total = entityManager.createQuery(countQuery).getSingleResult();
//
//        return new PageImpl<>(results, pageable, total);
//    }

    @Override
    public Page<CustomProductDto> findTopDiscountProducts(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductDto> cq = cb.createQuery(CustomProductDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.gt(root.get("discount"), 0));

        // calculate discount price
        Expression<Double> price = cb.coalesce(root.get("price"), 0.0);
        Expression<Double> discount = cb.coalesce(root.get("discount"), 0.0);

        // price * discount
        Expression<Double> priceTimesDiscount = cb.prod(price, discount);

        // (price * discount) / 100
        Expression<Number> discountAmountRaw = cb.quot(priceTimesDiscount, cb.literal(100.0));
        Expression<Double> discountAmount = discountAmountRaw.as(Double.class);

        // price - ((price * discount) / 100)
        Expression<Double> discountPrice = cb.diff(price, discountAmount);

        cq.select(cb.construct(
                        CustomProductDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.coalesce(cb.avg(reviewJoin.get("rating")), 0),
                        root.get("price"),
                        root.get("discount"),
                        discountPrice,
                        root.get("createdAt"),
                        root.get("updatedAt")

                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), root.get("slug"), root.get("price"), imageJoin, root.get("createdAt"), root.get("updatedAt"))
                .orderBy(cb.desc(root.get("discount")));

        TypedQuery<CustomProductDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.gt(countRoot.get("discount"), 0));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductDto> findTopDiscountProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductDto> cq = cb.createQuery(CustomProductDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
        predicates.add(cb.gt(root.get("discount"), 0));

        // calculate discount price
        Expression<Double> price = cb.coalesce(root.get("price"), 0.0);
        Expression<Double> discount = cb.coalesce(root.get("discount"), 0.0);

        // price * discount
        Expression<Double> priceTimesDiscount = cb.prod(price, discount);

        // (price * discount) / 100
        Expression<Number> discountAmountRaw = cb.quot(priceTimesDiscount, cb.literal(100.0));
        Expression<Double> discountAmount = discountAmountRaw.as(Double.class);

        // price - ((price * discount) / 100)
        Expression<Double> discountPrice = cb.diff(price, discountAmount);

        cq.select(cb.construct(
                        CustomProductDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        cb.coalesce(cb.avg(reviewJoin.get("rating")), 0),
                        root.get("price"),
                        root.get("discount"),
                        discountPrice,
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"), root.get("name"), root.get("slug"), root.get("price"), imageJoin, root.get("createdAt"),  root.get("updatedAt"))
                .orderBy(cb.desc(root.get("discount")));

        TypedQuery<CustomProductDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);
        countPredicates.add(cb.gt(countRoot.get("discount"), 0));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductStockDto> findLowStockProducts(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductStockDto> cq = cb.createQuery(CustomProductStockDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages");
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(ProductPredicates.lowStockPredicate(root, cb));

        cq.select(cb.construct(
                        CustomProductStockDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        root.get("quantity"),
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(root.get("quantity")));

        TypedQuery<CustomProductStockDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductStockDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(ProductPredicates.lowStockPredicate(countRoot, cb));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductStockDto> findLowStockProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductStockDto> cq = cb.createQuery(CustomProductStockDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages");
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
        predicates.add(ProductPredicates.lowStockPredicate(root, cb));

        cq.select(cb.construct(
                        CustomProductStockDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        root.get("quantity"),
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        // sort
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            Path<?> sortPath = root.get(sortOrder.getProperty());
            Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
            orders.add(order);
        }
        cq.orderBy(orders);

        TypedQuery<CustomProductStockDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductStockDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);
        countPredicates.add(ProductPredicates.lowStockPredicate(countRoot, cb));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CustomProductDiscountDto> findDiscountProducts(ProductFilterDto productFilterDto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // query result
        CriteriaQuery<CustomProductDiscountDto> cq = cb.createQuery(CustomProductDiscountDto.class);
        Root<Product> root = cq.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages");
        imageJoin.on(cb.isTrue(imageJoin.get("isMain")));

        List<Predicate> predicates = this.buildPredicates(root, cb, productFilterDto);
        predicates.add(ProductPredicates.hasDiscountPredicate(root, cb));

        cq.select(cb.construct(
                        CustomProductDiscountDto.class,
                        root.get("id"),
                        imageJoin,
                        root.get("name"),
                        root.get("slug"),
                        root.get("discount"),
                        root.get("createdAt"),
                        root.get("updatedAt")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        // sort
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            Path<?> sortPath = root.get(sortOrder.getProperty());
            Order order = sortOrder.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath);
            orders.add(order);
        }
        cq.orderBy(orders);

        TypedQuery<CustomProductDiscountDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<CustomProductDiscountDto> results = query.getResultList();

        // count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = this.buildPredicates(countRoot, cb, productFilterDto);
        countPredicates.add(ProductPredicates.hasDiscountPredicate(countRoot, cb));

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
}
