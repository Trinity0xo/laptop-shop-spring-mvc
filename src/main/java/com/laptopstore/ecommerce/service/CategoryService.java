package com.laptopstore.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.category.CategoryCriteriaDto;
import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.repository.CategoryRepository;
import com.laptopstore.ecommerce.specification.CategorySpecification;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PageableService pageableService;

    public CategoryService(CategoryRepository categoryRepository, PageableService pageableService) {
        this.categoryRepository = categoryRepository;
        this.pageableService = pageableService;
    }

    public void handleCreateCategory(CreateCategoryDto createCategoryDto, String imageName) {
        Category newCategory = new Category();
        newCategory.setImage(imageName);
        newCategory.setName(createCategoryDto.getName());
        newCategory.setDescription(createCategoryDto.getDescription());

        this.categoryRepository.save(newCategory);
    }

    public void handleUpdateCategory(UpdateCategoryDto updateCategoryDto, Category category) {
        category.setName(updateCategoryDto.getName());
        category.setDescription(updateCategoryDto.getDescription());

        this.categoryRepository.save(category);
    }

    public void handleDeleteCategoryById(Long id) {
        this.categoryRepository.deleteById(id);
    }

    public Category handleGetCategoryById(Long id) {
        return this.categoryRepository.findById(id).orElse(null);
    }

    public List<Category> handleGetAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Page<Category> handleGetAllCategories(CategoryCriteriaDto categoryCriteriaDto) {
        Specification<Category> specification = Specification.where(null);

        Pageable pageable = pageableService.handleCreatePageable(categoryCriteriaDto.getIntegerPage(),
                categoryCriteriaDto.getIntegerLimit(), categoryCriteriaDto.getSortBy(),
                categoryCriteriaDto.getEnumSortDirection());

        if (categoryCriteriaDto.getName() != null &&
                !categoryCriteriaDto.getName().isEmpty()) {
            Specification<Category> currentSpecification = CategorySpecification
                    .nameLike(categoryCriteriaDto.getName());
            specification = specification.and(currentSpecification);
        }

        return this.categoryRepository.findAll(specification, pageable);
    }

    public Category handleGetCategoryByName(String name){
        return this.categoryRepository.findByName(name).orElse(null);
    }
}
