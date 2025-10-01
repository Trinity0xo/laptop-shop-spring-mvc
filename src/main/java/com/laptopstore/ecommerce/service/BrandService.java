package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.brand.BrandFilterDto;
import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.Brand;

import java.util.List;

public interface BrandService {
    Brand getBrandById(long brandId);
    Brand getBrandBySlug(String brandSlug);
    PageResponse<List<Brand>> getAllBrands(BrandFilterDto brandFilterDto);
    void createBrand(CreateBrandDto createBrandDto);
    UpdateBrandDto getInformationForUpdateBrand(long brandId);
    void updateBrand(UpdateBrandDto updateBrandDto);
    void deleteBrand(long brandId);
}
