package com.laptopstore.ecommerce.util.constant;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    PENDING("Đang xử lý"),
    CONFIRMED("Đã xác nhận"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OrderStatusEnum(String displayName) {
        this.displayName = displayName;
    }
}
