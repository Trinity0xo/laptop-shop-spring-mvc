package com.laptopstore.ecommerce.util.error;

public class RoleNotFoundException extends EntityNotFoundException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy vai trò";

    public RoleNotFoundException(){
    }

    public RoleNotFoundException(String redirectUrl) {
        super(DEFAULT_MESSAGE, redirectUrl);
    }
}
