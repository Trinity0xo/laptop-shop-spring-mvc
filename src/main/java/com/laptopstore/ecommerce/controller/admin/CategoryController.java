package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.service.FileService;
import com.laptopstore.ecommerce.service.UploadFoldersService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final FileService fileService;
    private final UploadFoldersService uploadFoldersService;

    public CategoryController(CategoryService categoryService,
                              FileService fileService,
                              UploadFoldersService uploadFoldersService) {
        this.categoryService = categoryService;
        this.fileService = fileService;
        this.uploadFoldersService = uploadFoldersService;
    }

    @GetMapping("")
    public String showCategoryPage(
            Model model,
            CategoryCriteriaDto categoryCriteriaDto
    ) throws Exception {
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
            Model model
    ) throws Exception {

        model.addAttribute("createCategoryDto", new CreateCategoryDto());

        return "/admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            @Valid CreateCategoryDto createCategoryDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (bindingResult.hasErrors()) {
            return "/admin/category/create";
        }

        String categoryPicturesFolderName = this.uploadFoldersService.handleGetCategoryPicturesFolderName();

        String imageName = this.fileService.handleUploadFile(createCategoryDto.getImage(), categoryPicturesFolderName);

        this.categoryService.handleCreateCategory(createCategoryDto, imageName);

        redirectAttributes.addFlashAttribute("successMessage", "Category created successfully");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {

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
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "deleteImageName", required = false) String deleteImageName
    ) throws Exception {

        if (bindingResult.hasErrors()) {
            return "/admin/category/edit";
        }

        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        String categoryPicturesFolderName = this.uploadFoldersService.handleGetCategoryPicturesFolderName();

        if (deleteImageName != null && !deleteImageName.isEmpty() && updateCategoryDto.getImage() != null && !updateCategoryDto.getImage().isEmpty()) {
            this.fileService.handleDeleteFile(deleteImageName, categoryPicturesFolderName);
        }

        if (updateCategoryDto.getImage() != null && !updateCategoryDto.getImage().isEmpty()) {
            String updatedProductImage = this.fileService.handleUploadFile(updateCategoryDto.getImage(), categoryPicturesFolderName);
            category.setImage(updatedProductImage);
        }

        this.categoryService.handleUpdateCategory(updateCategoryDto, category);

        redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteCategoryPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        model.addAttribute("category", category);
        return "/admin/category/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        Category category = categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        String categoryPicturesFolderName = this.uploadFoldersService.handleGetCategoryPicturesFolderName();

        this.fileService.handleDeleteFile(category.getImage(), categoryPicturesFolderName);

        this.categoryService.handleDeleteCategoryById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/details/{id}")
    public String showDetailsCategoryPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Category category = this.categoryService.handleGetCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        model.addAttribute("category", category);

        return "/admin/category/details";
    }
}
