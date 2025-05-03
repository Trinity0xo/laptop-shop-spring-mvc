package com.laptopstore.ecommerce.dto.order;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCriteriaDto extends PageableCriteriaDto {
    public int getFilterCount(){
        int count = 0;

        if (paymentMethods != null && !paymentMethods.isEmpty()) count++;
        if (orderStatus != null && !orderStatus.isEmpty()) count++;
        if (getDoubleMinTotalPrice() != null && getDoubleMaxTotalPrice() != null && getDoubleMinTotalPrice() <= getDoubleMaxTotalPrice()) count++;

        return count;
    }

    public String toQuery(){
        StringBuilder stringBuilder = new StringBuilder();

        if(getLongOrderId() != null && getLongOrderId() > 0){
            stringBuilder.append("&orderId=").append(orderId);
        }

        if (receiverEmail != null && !receiverEmail.isEmpty()) {
            stringBuilder.append("&receiverEmail=").append(receiverEmail);
        }

        if (getDoubleMinTotalPrice() != null && getDoubleMinTotalPrice() > 0) {
            stringBuilder.append("&minTotalPrice=").append(minTotalPrice);
        }

        if (getDoubleMaxTotalPrice() != null && getDoubleMaxTotalPrice() > 0) {
            stringBuilder.append("&maxTotalPrice=").append(maxTotalPrice);
        }

        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            for (String paymentMethod : paymentMethods) {
                stringBuilder.append("&paymentMethods=").append(paymentMethod);
            }
        }

        if (orderStatus != null && !orderStatus.isEmpty()) {
            for (String status : orderStatus) {
                stringBuilder.append("&orderStatus=").append(status);
            }
        }

        if(getIntegerPage() != null && getIntegerPage() > 0){
            stringBuilder.append("&page=").append(getPage());
        }

        if(getIntegerLimit() != null && getIntegerLimit()  > 0){
            stringBuilder.append("&limit=").append(getLimit());
        }

        if (getSortBy() != null && !getSortBy().isEmpty()) {
            stringBuilder.append("&sortBy=").append(getSortBy());
        }

        if (getEnumSortDirection() != null) {
            stringBuilder.append("&sortDirection=").append(getSortDirection());
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.setCharAt(0, '?');
        }

        return stringBuilder.toString();
    }

    public Long getLongOrderId(){
        try{
            return Long.parseLong(orderId);
        }catch (Exception e){
            return -1L;
        }
    }

    public Double getDoubleMinTotalPrice(){
        try{
            return Double.parseDouble(minTotalPrice);
        }catch (Exception e){
            return null;
        }
    }

    public Double getDoubleMaxTotalPrice(){
        try{
            return Double.parseDouble(maxTotalPrice);
        }catch (Exception e){
           return null;
        }
    }

    private String orderId;
    private String receiverEmail;
    private List<String> paymentMethods;
    private List<String> orderStatus;
    private String minTotalPrice;
    private String maxTotalPrice;
}
