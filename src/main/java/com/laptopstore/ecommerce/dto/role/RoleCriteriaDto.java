package com.laptopstore.ecommerce.dto.role;

import com.laptopstore.ecommerce.dto.PageableCriteriaDto;
import com.laptopstore.ecommerce.util.constant.SortDirectionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCriteriaDto extends PageableCriteriaDto {
    private String name;
}
