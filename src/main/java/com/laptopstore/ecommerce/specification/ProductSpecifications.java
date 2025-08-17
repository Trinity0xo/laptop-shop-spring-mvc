package com.laptopstore.ecommerce.specification;

import com.laptopstore.ecommerce.specification.predicate.ProductPredicates;
import com.laptopstore.ecommerce.util.constant.StockStatusEnum;
import org.springframework.data.jpa.domain.Specification;

import com.laptopstore.ecommerce.model.Product;

import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> similarCpu(String cpu){
        return (root, query, criteriaBuilder) -> ProductPredicates.similarCpuPredicate(root, criteriaBuilder, cpu);
    }

    public static Specification<Product> similarGpu(String gpu){
        return (root, query, criteriaBuilder) -> ProductPredicates.similarGpuPredicate(root, criteriaBuilder, gpu);
    }

    public static Specification<Product> similarRam(String ram){
        return (root, query, criteriaBuilder) -> ProductPredicates.similarRamPredicate(root, criteriaBuilder, ram);
    }

    public static Specification<Product> similarStorage(String storage){
        return (root, query, criteriaBuilder) -> ProductPredicates.similarStoragePredicate(root, criteriaBuilder, storage);
    }

    public static Specification<Product> likeCpuBrands(List<String> cpuBrands) {
        return (root, query, criteriaBuilder) -> ProductPredicates.likeCpuBrandsPredicate(root, criteriaBuilder, cpuBrands);
    }

    public static Specification<Product> likeGpuBrands(List<String> gpuBrands) {
        return (root, query, criteriaBuilder) -> ProductPredicates.likeGpuBrandsPredicate(root, criteriaBuilder, gpuBrands);
    }

    public static Specification<Product> likeRamSizes(List<String> ramSizes) {
        return (root, query, criteriaBuilder) -> ProductPredicates.likeRamSizesPredicate(root, criteriaBuilder, ramSizes);
    }

    public static Specification<Product> likeStorgeSizes(List<String> storageSizes) {
        return (root, query, criteriaBuilder) -> ProductPredicates.likeStorageSizesPredicate(root, criteriaBuilder, storageSizes);
    }

    public static Specification<Product> hasDiscount() {
        return (root, query, criteriaBuilder) -> ProductPredicates.hasDiscountPredicate(root, criteriaBuilder);
    }

    public static Specification<Product> equalCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> ProductPredicates.equalCategoryPredicate(root, criteriaBuilder, categoryName);
    }

    public static Specification<Product> equalBrand(String brandName) {
        return (root, query, criteriaBuilder) -> ProductPredicates.equalBrandPredicate(root, criteriaBuilder, brandName);
    }

    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> ProductPredicates.nameLikePredicate(root, criteriaBuilder, name);
    }

    public static Specification<Product> categoryIn(List<String> categoryNames) {
        return (root, query, criteriaBuilder) -> ProductPredicates.categoryInPredicate(root, criteriaBuilder, categoryNames);
    }

    public static Specification<Product> brandIn(List<String> brandNames) {
        return (root, query, criteriaBuilder) -> ProductPredicates.brandInPredicate(root, criteriaBuilder, brandNames);
    }

    public static Specification<Product> minPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> ProductPredicates.minPricePredicate(root, criteriaBuilder, minPrice);
    }

    public static Specification<Product> maxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> ProductPredicates.maxPricePredicate(root, criteriaBuilder, maxPrice);
    }

    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> ProductPredicates.priceBetweenPredicate(root, criteriaBuilder, minPrice, maxPrice);
    }

    public static Specification<Product> discountPriceBetween(Double minDiscountPrice, Double maxDiscountPrice) {
        return (root, query, criteriaBuilder) -> ProductPredicates.discountPriceBetweenPredicate(root, criteriaBuilder, minDiscountPrice, maxDiscountPrice);
    }

    public static Specification<Product> stockStatus(StockStatusEnum stockStatus) {
        return (root, query, criteriaBuilder) -> ProductPredicates.stockStatusPredicate(root, criteriaBuilder, stockStatus);
    }

    public static Specification<Product> lowStock() {
        return (root, query, criteriaBuilder) -> ProductPredicates.lowStockPredicate(root, criteriaBuilder);
    }
}
