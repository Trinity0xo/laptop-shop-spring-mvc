package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.dto.pagination.PageableDto;

import com.laptopstore.ecommerce.model.Order;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.TypeParseUtils;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.DateTimeUtils.getValidInstantRange;
import static com.laptopstore.ecommerce.util.EnumUtils.getEnumValues;
import static com.laptopstore.ecommerce.util.PriceUtils.getValidPriceRange;
import static com.laptopstore.ecommerce.util.PriceUtils.parsePrice;
import static com.laptopstore.ecommerce.util.TypeParseUtils.parseStringInstantOrNull;

@Getter
@Setter
public class OrderFilterDto extends PageableDto {
    public static final double MIN_TOTAL_PRICE = 0;
    public static final double MAX_TOTAL_PRICE = 100000000000D;

    private List<String> paymentMethods;
    private List<String> statuses;
    private String minTotalPrice;
    private String maxTotalPrice;
    private String startDate;
    private String endDate;

    public Instant getInstantStartDate(){
        return parseStringInstantOrNull(startDate);
    }

    public Instant getInstantEndDate(){
        return parseStringInstantOrNull(endDate);
    }

    public double getDoubleMinTotalPrice() {
        return parsePrice(minTotalPrice, MIN_TOTAL_PRICE, MIN_TOTAL_PRICE, MAX_TOTAL_PRICE);
    }

    public double getDoubleMaxTotalPrice() {
        return parsePrice(maxTotalPrice, MAX_TOTAL_PRICE, MIN_TOTAL_PRICE, MAX_TOTAL_PRICE);
    }

    public List<OrderStatusEnum> getEnumOrderStatus(){
        return getEnumValues(statuses, OrderStatusEnum.class);
    }

    public List<PaymentMethodEnum> getEnumPaymentMethods(){
        return getEnumValues(paymentMethods, PaymentMethodEnum.class);
    }

    public int getFilterCount() {
        int count = 0;

        if (getEnumOrderStatus() != null && !getEnumOrderStatus().isEmpty()) count++;
        if (getEnumPaymentMethods() != null && !getEnumPaymentMethods().isEmpty()) count++;
        if (!getValidPriceRange(getDoubleMinTotalPrice(), getDoubleMaxTotalPrice(), MIN_TOTAL_PRICE, MAX_TOTAL_PRICE).isEmpty()) count++;
        if (!getValidInstantRange(getInstantStartDate(), getInstantEndDate()).isEmpty()) count++;

        return count;
    }

    public String toQuery() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        if(getSearch() != null && !getSearch().isEmpty()){
            builder.queryParam("search", getSearch());
        }

        Map<String, Double> priceRange = getValidPriceRange(getDoubleMinTotalPrice(),getDoubleMaxTotalPrice(), MIN_TOTAL_PRICE, MAX_TOTAL_PRICE);

        if (!priceRange.isEmpty()){
            builder.queryParam("minTotalPrice", priceRange.get("min"));
            builder.queryParam("maxTotalPrice", priceRange.get("max"));
        }

        Map<String, Instant> instantRange = getValidInstantRange(getInstantStartDate(), getInstantEndDate());

        if(!instantRange.isEmpty()){
            builder.queryParam("startDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("startDate")));
            builder.queryParam("endDate", TypeParseUtils.parseInstantLocalDateOrNull(instantRange.get("endDate")));
        }

        if (getEnumPaymentMethods() != null && !getEnumPaymentMethods().isEmpty()) {
            for (PaymentMethodEnum paymentMethod : getEnumPaymentMethods()) {
                builder.queryParam("paymentMethods", paymentMethod);
            }
        }

        if (getEnumOrderStatus() != null && !getEnumOrderStatus().isEmpty()) {
            for (OrderStatusEnum status : getEnumOrderStatus()) {
                builder.queryParam("orderStatuses", status);
            }
        }

        if (getIntegerPage() != null && getIntegerPage() > 0) {
            builder.queryParam("page", getIntegerPage());
        }

        if (getIntegerLimit() != null && getIntegerLimit()> 0) {
            builder.queryParam("limit", getIntegerLimit());
        }

        String sortField = PaginationUtils.getValidSortBy(Order.class, getSortBy(), Order.DEFAULT_SORT_FIELD);

        if (sortField != null) {
            builder.queryParam("sortBy", sortField);
        }

        if (getEnumSortDirection() != null) {
            builder.queryParam("sortDirection", getEnumSortDirection());
        }

        return builder.build().encode().toUriString();
    }
}
