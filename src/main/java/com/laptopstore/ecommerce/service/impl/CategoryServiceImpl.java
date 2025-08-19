package com.laptopstore.ecommerce.service.impl;

import java.util.List;

import com.laptopstore.ecommerce.dto.category.CategoryFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.CategoryService;
import com.laptopstore.ecommerce.service.FileService;
import com.laptopstore.ecommerce.service.FolderService;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.error.CategoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.repository.CategoryRepository;
import com.laptopstore.ecommerce.specification.CategorySpecifications;

import static com.laptopstore.ecommerce.util.SlugUtils.toSlug;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FolderService folderService;
    private final FileService fileService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, FolderService folderService, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.folderService = folderService;
        this.fileService = fileService;
    }

    @Override
    public PageResponse<List<Category>> getAllCategories(CategoryFilterDto categoryFilterDto){
        Specification<Category> specification = Specification.where(null);

        Pageable pageable = PaginationUtils.createPageable(
                categoryFilterDto.getIntegerPage(),
                categoryFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(Category.class, categoryFilterDto.getSortBy(), Category.DEFAULT_SORT_FIELD),
                categoryFilterDto.getEnumSortDirection()
        );

        if(categoryFilterDto.getSearch() != null && !categoryFilterDto.getSearch().isEmpty()){
            Specification<Category> currentSpecification = CategorySpecifications.nameLike(categoryFilterDto.getSearch());
            specification = specification.and(currentSpecification);
        }

        Page<Category> categories = this.categoryRepository.findAll(specification, pageable);

        return new PageResponse<>(
                categories.getPageable().getPageNumber() + 1,
                categories.getTotalPages(),
                categories.getTotalElements(),
                categories.getContent()
        );
    }

    @Override
    public void createCategory(CreateCategoryDto createCategoryDto) {
        String categoryPicturesFolderName = this.folderService.getCategoryPicturesFolderName();

        String imageName = this.fileService.uploadFile(createCategoryDto.getNewImage(), categoryPicturesFolderName);

        Category newCategory = new Category(
                imageName,
                createCategoryDto.getName(),
                createCategoryDto.getDescription(),
                toSlug(createCategoryDto.getName())
        );

        this.categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long categoryId){
        Category category = this.categoryRepository.findById(categoryId).orElse(null);
        if(category == null){
            throw new CategoryNotFoundException();
        }

        return category;
    }

    @Override
    public UpdateCategoryDto getInformationForUpdateCategory(long categoryId){
        Category category = this.getCategoryById(categoryId);

        return new UpdateCategoryDto(
                category.getId(),
                category.getImage(),
                category.getName(),
                category.getDescription()
        );
    }

    @Override
    public void updateCategory(UpdateCategoryDto updateCategoryDto){
        Category category = this.getCategoryById(updateCategoryDto.getId());

        if(updateCategoryDto.getNewImage() != null && !updateCategoryDto.getNewImage().isEmpty()) {
            String categoryPicturesFolderName = this.folderService.getCategoryPicturesFolderName();
            String newImageName = this.fileService.uploadFile(updateCategoryDto.getNewImage(), categoryPicturesFolderName);
            this.fileService.deleteFile(category.getImage(), categoryPicturesFolderName);
            category.setImage(newImageName);
        }

        category.setName(updateCategoryDto.getName());
        category.setSlug(toSlug(updateCategoryDto.getName()));
        category.setDescription(updateCategoryDto.getDescription());

        this.categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long categoryId){
        Category category = this.getCategoryById(categoryId);
        String categoryPicturesFolderName = this.folderService.getCategoryPicturesFolderName();
        this.fileService.deleteFile(category.getImage(), categoryPicturesFolderName);
        this.categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryByName(String categoryName){
        return this.categoryRepository.findByName(categoryName).orElse(null);
    }
}
