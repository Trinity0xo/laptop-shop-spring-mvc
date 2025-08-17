package com.laptopstore.ecommerce.specification.predicate;

import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.util.constant.StockStatusEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ProductPredicates {
    public static final String OTHER_NAME = "other";
    public static final Number LOW_STOCK_QUANTITY = 10;

    public static Predicate similarCpuPredicate(Root<Product> root, CriteriaBuilder cb, String cpu) {
        String[] splitCpu = cpu.split(" ");

        Predicate[] predicates = new Predicate[splitCpu.length];
        for (int i = 0; i < splitCpu.length; i++) {
            predicates[i] = cb.like(
                    cb.lower(root.get("cpu")), "%" +  splitCpu[i].toLowerCase() + "%"
            );
        }
        return cb.or(predicates);
    }

    public static Predicate similarGpuPredicate(Root<Product> root, CriteriaBuilder cb, String gpu){
        String[] splitGpu = gpu.split(" ");
        Predicate[] predicates = new Predicate[splitGpu.length];
        for (int i = 0; i < splitGpu.length; i++) {
            predicates[i] = cb.like(
                    cb.lower(root.get("gpu")), "%" + splitGpu[i].toLowerCase() + "%"
            );
        }
        return cb.or(predicates);
    }

    public static Predicate similarRamPredicate(Root<Product> root, CriteriaBuilder cb, String ram){
        String[] splitRam = ram.split(" ");
        Predicate[] predicates = new Predicate[splitRam.length];
        for (int i = 0; i < splitRam.length; i++) {
            predicates[i] = cb.like(
                    cb.lower(root.get("ram")), "%" + splitRam[i].toLowerCase() + "%"
            );
        }
        return cb.or(predicates);
    }

    public static Predicate similarStoragePredicate(Root<Product> root, CriteriaBuilder cb, String storage){
        String[] splitStorage = storage.split(" ");
        Predicate[] predicates = new Predicate[splitStorage.length];
        for (int i = 0; i < splitStorage.length; i++) {
            predicates[i] = cb.like(
                    cb.lower(root.get("storage")), "%" + splitStorage[i].toLowerCase() + "%"
            );
        }
        return cb.or(predicates);
    }

    public static Predicate likeCpuBrandsPredicate(Root<Product> root, CriteriaBuilder cb, List<String> cpuBrands) {
        List<Predicate> predicates = new ArrayList<>();
        for (String brand : cpuBrands) {
            predicates.add(cb.like(cb.lower(root.get("cpu")), "%" + brand.toLowerCase() + "%"));
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate likeGpuBrandsPredicate(Root<Product> root, CriteriaBuilder cb, List<String> gpuBrands) {
        List<Predicate> predicates = new ArrayList<>();
        for (String brand : gpuBrands) {
            predicates.add(cb.like(cb.lower(root.get("gpu")), "%" + brand.toLowerCase() + "%"));
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate likeRamSizesPredicate(Root<Product> root, CriteriaBuilder cb, List<String> ramSizes) {
        List<Predicate> predicates = new ArrayList<>();
        for (String size : ramSizes) {
            predicates.add(cb.like(cb.lower(root.get("ram")), "%" + size.toLowerCase() + "%"));
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate likeStorageSizesPredicate(Root<Product> root, CriteriaBuilder cb, List<String> storageSizes) {
        List<Predicate> predicates = new ArrayList<>();
        for (String size : storageSizes) {
            predicates.add(cb.like(cb.lower(root.get("storage")), "%" + size.toLowerCase() + "%"));
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate hasDiscountPredicate(Root<Product> root, CriteriaBuilder cb) {
        return cb.gt(root.get("discount"), 0);
    }

    public static Predicate equalCategoryPredicate(Root<Product> root, CriteriaBuilder cb, String categorySlug) {
        if (!categorySlug.equalsIgnoreCase(OTHER_NAME)) {
            return cb.equal(cb.lower(root.get("category").get("slug")), categorySlug.toLowerCase());
        } else {
            return cb.isNull(root.get("category"));
        }
    }

    public static Predicate equalBrandPredicate(Root<Product> root, CriteriaBuilder cb, String brandSlug) {
        if (!brandSlug.equalsIgnoreCase(OTHER_NAME)) {
            return cb.equal(cb.lower(root.get("brand").get("slug")), brandSlug.toLowerCase());
        } else {
            return cb.isNull(root.get("brand"));
        }
    }


    public static Predicate nameLikePredicate(Root<Product> root, CriteriaBuilder cb, String name) {
        return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Predicate categoryInPredicate(Root<Product> root, CriteriaBuilder cb, List<String> categorySlugs) {
        List<Predicate> predicates = new ArrayList<>();

        for (String slug : categorySlugs) {
            if (slug.equalsIgnoreCase(OTHER_NAME)) {
                predicates.add(cb.isNull(root.get("category")));
            } else {
                predicates.add(cb.equal(cb.lower(root.get("category").get("slug")), slug.toLowerCase()));
            }
        }

        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate brandInPredicate(Root<Product> root, CriteriaBuilder cb, List<String> brandSlugs) {
        List<Predicate> predicates = new ArrayList<>();

        for (String slug : brandSlugs) {
            if (slug.equalsIgnoreCase(OTHER_NAME)) {
                predicates.add(cb.isNull(root.get("brand")));
            } else {
                predicates.add(cb.equal(cb.lower(root.get("brand").get("slug")), slug.toLowerCase()));
            }
        }

        return cb.or(predicates.toArray(new Predicate[0]));
    }

    public static Predicate minPricePredicate(Root<Product> root, CriteriaBuilder cb, Double minPrice) {
        return cb.ge(root.get("price"), minPrice);
    }

    public static Predicate maxPricePredicate(Root<Product> root, CriteriaBuilder cb, Double maxPrice) {
        return cb.le(root.get("price"), maxPrice);
    }

    public static Predicate priceBetweenPredicate(Root<Product> root, CriteriaBuilder cb, Double minPrice, Double maxPrice) {
        return cb.and(
                cb.ge(root.get("price"), minPrice),
                cb.le(root.get("price"), maxPrice)
        );
    }

    public static Predicate discountPriceBetweenPredicate(Root<Product> root, CriteriaBuilder cb, Double minDiscount, Double maxDiscount) {
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

        return cb.between(discountPrice, minDiscount, maxDiscount);
    }

    public static Predicate stockStatusPredicate(Root<Product> root, CriteriaBuilder cb, StockStatusEnum stockStatus) {
        if (stockStatus == StockStatusEnum.OUT_OF_STOCK) {
            return cb.le(root.get("quantity"), 0);
        } else if (stockStatus == StockStatusEnum.IN_STOCK) {
            return cb.gt(root.get("quantity"), 0);
        } else {
            return cb.conjunction(); // no filter
        }
    }

    public static Predicate lowStockPredicate(Root<Product> root, CriteriaBuilder cb) {
        return cb.le(root.get("quantity"), LOW_STOCK_QUANTITY);
    }
}
