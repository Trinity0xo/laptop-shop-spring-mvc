package com.laptopstore.ecommerce.util.error;

public class CategoryNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy Danh mục";

    public CategoryNotFoundException() {
        super();
    }

    public CategoryNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
