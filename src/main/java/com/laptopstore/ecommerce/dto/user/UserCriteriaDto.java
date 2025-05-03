package com.laptopstore.ecommerce.dto.user;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCriteriaDto extends PageableCriteriaDto {
    private String email;
}
