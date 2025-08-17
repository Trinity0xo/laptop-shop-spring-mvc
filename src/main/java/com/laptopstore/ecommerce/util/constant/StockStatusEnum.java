package com.laptopstore.ecommerce.util.constant;

import lombok.Getter;

@Getter
public enum StockStatusEnum {
    ALL("Tất cả"),
    IN_STOCK("Còn hàng"),
    OUT_OF_STOCK("Hết hàng");

    private final String displayName;

    StockStatusEnum(String displayName) {
        this.displayName = displayName;
    }
}
