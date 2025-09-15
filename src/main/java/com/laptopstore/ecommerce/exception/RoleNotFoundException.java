package com.laptopstore.ecommerce.exception;

import lombok.Getter;

@Getter
public class RoleNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Không tìm thấy vai trò";
    private Long roleId;
    private String roleSlug;

    public RoleNotFoundException(Long roleId) {
        super(DEFAULT_MESSAGE);
        this.roleId = roleId;
    }

    public RoleNotFoundException(String roleSlug) {
        super(DEFAULT_MESSAGE);
        this.roleSlug = roleSlug;
    }
}
