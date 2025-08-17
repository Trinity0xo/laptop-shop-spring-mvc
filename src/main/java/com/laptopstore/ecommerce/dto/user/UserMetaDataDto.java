package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserMetaDataDto {
    private List<Role> roles = new ArrayList<>();
}
