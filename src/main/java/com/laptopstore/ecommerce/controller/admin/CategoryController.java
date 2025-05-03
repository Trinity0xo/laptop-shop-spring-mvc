package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.service.FileService;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.laptopstore.ecommerce.dto.category.CategoryCriteriaDto;
import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/dashboard/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final FileService fileService;

    public CategoryController(CategoryService categoryService, FileService fileService) {
        this.categoryService = categoryService;
        this.fileService = fileService;
    }

    @GetMapping("")
    public String showCategoryPage(Model model, CategoryCriteriaDto categoryCriteriaDto) {
        Page<Category> categories = this.categoryService.handleGetAllCategories(categoryCriteriaDto);
        model.addAttribute("categoryList", categories.getContent());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("currentPage", categories.getPageable().getPageNumber() + 1);
        model.addAttribute("query", categoryCriteriaDto);
        model.addAttribute("resultCount", categories.getTotalElements());

        return "/admin/category/index";
    }

    @GetMapping("/create")
    public String showCreateCategoryPage(
            Model model) {

        model.addAttribute("createCategoryDto", new CreateCategoryDto());

        return "/admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            @Valid CreateCategoryDto createCategoryDto,
            BindingResult bindingResult
            ) {
        if (bindingResult.hasErrors()) {
            return "/admin/category/create";
        }

        String imageName = this.fileService.handleUploadFile(createCategoryDto.getImage(), "category_pictures");

        this.categoryService.handleCreateCategory(createCategoryDto, imageName);

        String successMessage = "Category created successfully";

        return "redirect:/dashboard/category?successMessage=" + successMessage;
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryPage(
            @PathVariable Long id, Model model)
            throws Exception {

        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(category.getId());
        updateCategoryDto.setName(category.getName());
        updateCategoryDto.setDescription(category.getDescription());

        model.addAttribute("updateCategoryDto", updateCategoryDto);
        model.addAttribute("categoryImage", category.getImage());

        return "/admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(
            @PathVariable Long id,
            @Valid UpdateCategoryDto updateCategoryDto,
            BindingResult bindingResult,
            @RequestParam(value = "deleteImageName", required = false) String deleteImageName
    )
            throws Exception {

        if (bindingResult.hasErrors()) {
            return "/admin/category/edit";
        }

        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        if(deleteImageName != null && !deleteImageName.isEmpty() && updateCategoryDto.getImage() != null && !updateCategoryDto.getImage().isEmpty()) {
            this.fileService.handleDeleteFile(deleteImageName, "category_pictures");
        }

        if(updateCategoryDto.getImage() != null && !updateCategoryDto.getImage().isEmpty()) {
            String updatedProductImage = this.fileService.handleUploadFile(updateCategoryDto.getImage(), "category_pictures");
            category.setImage(updatedProductImage);
        }

        this.categoryService.handleUpdateCategory(updateCategoryDto, category);

        String successMessage = "Category updated successfully";

        return "redirect:/dashboard/category?successMessage=" + successMessage;
    }

    @GetMapping("/delete/{id}")
    public String showDeleteCategoryPage(@PathVariable Long id,
            Model model)
            throws Exception {
        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        model.addAttribute("category", category);
        return "/admin/category/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id
    ) throws Exception {
        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        this.fileService.handleDeleteFile(category.getImage(), "category_pictures");

        this.categoryService.handleDeleteCategoryById(id);

        String successMessage = "Category deleted successfully";

        return "redirect:/dashboard/category?successMessage=" + successMessage;
    }

    @GetMapping("/details/{id}")
    public String showDetailsCategoryPage(
            @PathVariable Long id,
            Model model
    ) throws Exception{
        Category category = this.categoryService.handleGetCategoryById(id);
        if(category == null){
            throw new NotFoundException("Category not found");
        }

        model.addAttribute("category", category);

        return "/admin/category/details";
    }
}
