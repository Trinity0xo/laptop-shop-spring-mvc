package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.validation.product.CreateProductConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@CreateProductConstraint
public class CreateProductDto extends BaseProductDto {
    private List<MultipartFile> newImages;
    private ProductMetaDataDto productMetaData;
    private String slug;

    public CreateProductDto(ProductMetaDataDto productMetaData){
        this.productMetaData = productMetaData;
    }
}