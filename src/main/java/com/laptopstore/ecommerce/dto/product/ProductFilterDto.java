package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.dto.pagination.PageableDto;

import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.TypeParseUtils;
import com.laptopstore.ecommerce.util.constant.StockStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;
import static com.laptopstore.ecommerce.util.PriceUtils.getValidPriceRange;
import static com.laptopstore.ecommerce.util.PriceUtils.parsePrice;
import static com.laptopstore.ecommerce.util.TypeParseUtils.parseStringEnumOrDefault;
import static com.laptopstore.ecommerce.util.TypeParseUtils.parseStringInstantOrNull;

@Getter
@Setter
public class ProductFilterDto extends PageableDto {
    private static final double MIN_DISCOUNT = 0;
    private static final double MAX_DISCOUNT = 100;
    public static final double MIN_PRICE = 0;
    public static final double MAX_PRICE = 100000000;

    private List<String> categories;
    private List<String> brands;
    private String stockStatus;
    private String minPrice;
    private String maxPrice;
    private String minDiscountPrice;
    private String maxDiscountPrice;

    private List<String> cpuBrands;
    private List<String> gpuBrands;
    private List<String> ramSizes;
    private List<String> storageSizes;

    private String startDate;
    private String endDate;

    public Instant getInstantStartDate(){
        return parseStringInstantOrNull(startDate);
    }

    public Instant getInstantEndDate(){
        return parseStringInstantOrNull(endDate);
    }

    public double getDoubleMinPrice() {
        return parsePrice(minPrice, MIN_PRICE, MIN_PRICE, MAX_PRICE);
    }

    public double getDoubleMaxPrice() {
        return parsePrice(maxPrice, MAX_PRICE, MIN_PRICE, MAX_PRICE);
    }

    public double getDoubleMinDiscountPrice() {
        return parsePrice(minDiscountPrice, MIN_PRICE, MIN_PRICE, MAX_PRICE);
    }

    public double getDoubleMaxDiscountPrice() {
        return parsePrice(maxDiscountPrice, MAX_PRICE, MIN_PRICE, MAX_PRICE);
    }

    public StockStatusEnum getEnumStockStatus(){
        return parseStringEnumOrDefault(StockStatusEnum.class, stockStatus, StockStatusEnum.ALL);
    }

    public int getFilterCount() {
        int count = 0;

        if (categories != null && !categories.isEmpty()) count++;
        if (brands != null && !brands.isEmpty()) count++;
        if (cpuBrands != null && !cpuBrands.isEmpty()) count++;
        if (gpuBrands != null && !gpuBrands.isEmpty()) count++;
        if (ramSizes != null && !ramSizes.isEmpty()) count++;
        if (storageSizes != null && !storageSizes.isEmpty()) count++;

        if (!getValidPriceRange(getDoubleMinPrice(), getDoubleMaxPrice(), MIN_PRICE, MAX_PRICE).isEmpty()) count++;
        if (!getValidPriceRange(getDoubleMinDiscountPrice(), getDoubleMaxDiscountPrice(), MIN_PRICE, MAX_PRICE).isEmpty()) count++;
        if (!getEnumStockStatus().equals(StockStatusEnum.ALL)) count++;
        if (!getValidInstantRange(getInstantStartDate(), getInstantEndDate()).isEmpty()) count++;

        return count;
    }

    public String toQuery() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        if (getSearch() != null && !getSearch().isEmpty()) {
            builder.queryParam("search", getSearch());
        }

        if (cpuBrands != null && !cpuBrands.isEmpty()) {
            for (String cpuBrand : cpuBrands) {
                builder.queryParam("cpuBrands", cpuBrand);
            }
        }

        if (gpuBrands != null && !gpuBrands.isEmpty()) {
            for (String gpuBrand : gpuBrands) {
                builder.queryParam("gpuBrands", gpuBrand);
            }
        }

        if (ramSizes != null && !ramSizes.isEmpty()) {
            for (String ramSize : ramSizes) {
                builder.queryParam("ramSizes", ramSize);
            }
        }

        if (storageSizes != null && !storageSizes.isEmpty()) {
            for (String storageSize : storageSizes) {
                builder.queryParam("storageSizes", storageSize);
            }
        }

        if (categories != null && !categories.isEmpty()) {
            for (String category : categories) {
                builder.queryParam("categoryNames", category);
            }
        }

        if (brands != null && !brands.isEmpty()) {
            for (String brand : brands) {
                builder.queryParam("brandNames", brand);
            }
        }

        Map<String, Double> priceRange = getValidPriceRange(getDoubleMinPrice(),getDoubleMaxPrice(), MIN_PRICE, MAX_PRICE);

        if (!priceRange.isEmpty()){
            builder.queryParam("minPrice", priceRange.get("min"));
            builder.queryParam("maxPrice", priceRange.get("max"));
        }

        Map<String, Double> discountPriceRange = getValidPriceRange(getDoubleMinPrice(),getDoubleMaxPrice(), MIN_PRICE, MAX_PRICE);

        if (!discountPriceRange.isEmpty()){
            builder.queryParam("minDiscountPrice", discountPriceRange.get("min"));
            builder.queryParam("maxDiscountPrice", discountPriceRange.get("max"));
        }

        Map<String, Instant> instantRange = getValidInstantRange(getInstantStartDate(), getInstantEndDate());

        if(!instantRange.isEmpty()){
            builder.queryParam("startDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("startDate")));
            builder.queryParam("endDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("endDate")));
        }

        if(getEnumStockStatus() != null && !getEnumStockStatus().equals(StockStatusEnum.ALL)){
            builder.queryParam("stockStatus", getEnumStockStatus());
        }

        if (getIntegerPage() != null &&  getIntegerPage() > 0) {
            builder.queryParam("page",  getIntegerPage());
        }

        if (getIntegerLimit() != null && getIntegerLimit() > 0) {
            builder.queryParam("limit", getIntegerLimit());
        }

        String sortField = PaginationUtils.getValidSortBy(Product.class, getSortBy(), Product.DEFAULT_SORT_FIELD);

        if (sortField != null) {
            builder.queryParam("sortBy", sortField);
        }

        if (getEnumSortDirection() != null) {
            builder.queryParam("sortDirection", getEnumSortDirection());
        }

        return builder.build().encode().toUriString();
    }
}