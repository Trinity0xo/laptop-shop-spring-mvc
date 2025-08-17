package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.util.validation.product.CreateProductConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@CreateProductConstraint
public class CreateProductDto extends BaseProductDto {
    private List<MultipartFile> newImages;
    private ProductMetaDataDto productMetaData;
    private String slug;

    public CreateProductDto(ProductMetaDataDto productMetaData){
        this.productMetaData = productMetaData;
    }
}