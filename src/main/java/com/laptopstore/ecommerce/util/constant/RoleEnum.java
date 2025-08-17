package com.laptopstore.ecommerce.util.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    SUPER_ADMIN("Quản trị viên"),
    OWNER("Chủ cửa hàng"),
    USER("Người dùng");

    private final String displayName;
    RoleEnum(String displayName) {
        this.displayName = displayName;
    }
}
