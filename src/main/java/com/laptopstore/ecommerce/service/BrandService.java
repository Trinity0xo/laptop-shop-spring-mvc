package com.laptopstore.ecommerce.service;

import java.util.Arrays;
import java.util.List;

import com.laptopstore.ecommerce.util.SortFields;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.brand.BrandCriteriaDto;
import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.repository.BrandRepository;
import com.laptopstore.ecommerce.specification.BrandSpecification;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final PageableService pageableService;

    public BrandService(BrandRepository brandRepository, PageableService pageableService) {
        this.brandRepository = brandRepository;
        this.pageableService = pageableService;
    }

    public void handleCreateBrand(CreateBrandDto createBrandDto) {
        Brand newBrand = new Brand();
        newBrand.setName(createBrandDto.getName());
        newBrand.setDescription(createBrandDto.getDescription());

        this.brandRepository.save(newBrand);
    }

    public void handleUpdateBrand(UpdateBrandDto updateBrandDto, Brand brand) {
        brand.setName(updateBrandDto.getName());
        brand.setDescription(updateBrandDto.getDescription());

        this.brandRepository.save(brand);
    }

    public void handleDeleteBrandById(Long id) {
        this.brandRepository.deleteById(id);
    }

    public Brand handleGetBrandById(Long id) {
        return this.brandRepository.findById(id).orElse(null);
    }

    public List<Brand> handleGetAllBrands() {
        return this.brandRepository.findAll();
    }

    public Page<Brand> handleGetAllBrands(BrandCriteriaDto brandCriteriaDto) {
        Specification<Brand> specification = Specification.where(null);

        List<String> validSortBy = SortFields.getValidSortFields(Brand.class);

        Pageable pageable = pageableService.handleCreatePageable(brandCriteriaDto.getIntegerPage(),
                brandCriteriaDto.getIntegerLimit(), brandCriteriaDto.getSortBy(),
                brandCriteriaDto.getEnumSortDirection(), validSortBy);

        if (brandCriteriaDto.getName() != null &&
                !brandCriteriaDto.getName().isEmpty()) {
            Specification<Brand> currentSpecification = BrandSpecification
                    .nameLike(brandCriteriaDto.getName());
            specification = specification.and(currentSpecification);
        }

        return this.brandRepository.findAll(specification, pageable);
    }

    public Brand handleGetBrandByName(String name){
        return this.brandRepository.findByName(name).orElse(null);
    }
}
