package com.laptopstore.ecommerce.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseUserInfoDto {
    protected String firstName;
    protected String lastName;
    protected String address;
    protected String phone;
}
