package com.laptopstore.ecommerce.dto.brand;

import com.laptopstore.ecommerce.util.validation.brand.CreateBrandConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@CreateBrandConstraint
public class CreateBrandDto extends BaseBrandDto {
}
