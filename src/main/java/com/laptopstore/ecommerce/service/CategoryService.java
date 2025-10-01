package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.category.CategoryFilterDto;
import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(long categoryId);
    Category getCategoryBySlug(String categorySlug);
    PageResponse<List<Category>> getAllCategories(CategoryFilterDto categoryFilterDto);
    void createCategory(CreateCategoryDto createCategoryDto);
    UpdateCategoryDto getInformationForUpdateCategory(long categoryId);
    void updateCategory(UpdateCategoryDto updateCategoryDto);
    void deleteCategory(long categoryId);
}
