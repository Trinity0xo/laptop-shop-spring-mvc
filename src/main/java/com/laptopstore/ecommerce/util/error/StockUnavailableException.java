package com.laptopstore.ecommerce.util.error;

public class StockUnavailableException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Số lượng sản phẩm trong kho không đủ";
    String redirectUrl;

    public StockUnavailableException(String redirectUrl) {
        super(DEFAULT_MESSAGE);
        this.redirectUrl = redirectUrl;
    }
}
