package com.laptopstore.ecommerce.service.impl;

import java.util.List;

import com.laptopstore.ecommerce.dto.brand.BrandFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.BrandService;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.error.BrandNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.repository.BrandRepository;
import com.laptopstore.ecommerce.specification.BrandSpecifications;

import static com.laptopstore.ecommerce.util.SlugUtils.toSlug;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public PageResponse<List<Brand>> getAllBrands(BrandFilterDto brandFilterDto){
        Specification<Brand> specification = Specification.where(null);

        Pageable pageable = PaginationUtils.createPageable(
                brandFilterDto.getIntegerPage(),
                brandFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Brand.class, brandFilterDto.getSortBy(), Brand.DEFAULT_SORT_FIELD),
                brandFilterDto.getEnumSortDirection()
        );

        if(brandFilterDto.getSearch() != null && !brandFilterDto.getSearch().isEmpty()){
            Specification<Brand> currentSpecification = BrandSpecifications.nameLike(brandFilterDto.getSearch());
            specification = specification.and(currentSpecification);
        }

        Page<Brand> bands = this.brandRepository.findAll(specification, pageable);

        return new PageResponse<>(
                bands.getPageable().getPageNumber() + 1,
                bands.getTotalPages(),
                bands.getTotalElements(),
                bands.getContent()
        );
    }

    @Override
    public void createBrand(CreateBrandDto createBrandDto) {
        Brand newBrand = new Brand(
                createBrandDto.getName(),
                createBrandDto.getDescription(),
                toSlug(createBrandDto.getName())
        );

        this.brandRepository.save(newBrand);
    }

    @Override
    public UpdateBrandDto getInformationForUpdateBrand(long brandId){
        Brand brand = this.getBrandById(brandId);

        return new UpdateBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getDescription()
        );
    }

    @Override
    public void updateBrand(UpdateBrandDto updateBrandDto) {
        Brand brand = this.getBrandById(updateBrandDto.getId());

        brand.setName(updateBrandDto.getName());
        brand.setSlug(toSlug(updateBrandDto.getName()));
        brand.setDescription(updateBrandDto.getDescription());

        this.brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(long brandId) {
        this.brandRepository.deleteById(brandId);
    }

    @Override
    public Brand getBrandById(long brandId){
        Brand brand = this.brandRepository.findById(brandId).orElse(null);
        if(brand == null){
            throw new BrandNotFoundException("/dashboard/brand");
        }

        return brand;
    }

    @Override
    public Brand getBrandByName(String brandName){
        return this.brandRepository.findByName(brandName).orElse(null);
    }
}
