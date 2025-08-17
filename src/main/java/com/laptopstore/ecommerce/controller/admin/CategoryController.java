package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.category.CategoryFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.laptopstore.ecommerce.dto.category.CreateCategoryDto;
import com.laptopstore.ecommerce.dto.category.UpdateCategoryDto;
import com.laptopstore.ecommerce.model.Category;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String showCategoryPage(
            Model model,
            CategoryFilterDto categoryFilterDto
    )  {
        PageResponse<List<Category>> response = this.categoryService.getAllCategories(categoryFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("categoryCriteriaDto", categoryFilterDto);
        return "/admin/category/index";
    }

    @GetMapping("/create")
    public String showCreateCategoryPage(
            Model model
    )  {
        model.addAttribute("createCategoryDto", new CreateCategoryDto());

        return "/admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            @Valid CreateCategoryDto createCategoryDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )  {
        if (bindingResult.hasErrors()) {
            return "/admin/category/create";
        }

        this.categoryService.createCategory(createCategoryDto);

        redirectAttributes.addFlashAttribute("successMessage", "Tạo loại sản phẩm thành công");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/update/{categoryId}")
    public String showEditCategoryPage(
            @PathVariable Long categoryId,
            Model model
    )  {
        UpdateCategoryDto updateCategoryDto = this.categoryService.getInformationForUpdateCategory(categoryId);
        model.addAttribute("updateCategoryDto", updateCategoryDto);

        return "/admin/category/update";
    }

    @PostMapping("/update")
    public String updateCategory(
            @Valid UpdateCategoryDto updateCategoryDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    )  {
        if (bindingResult.hasErrors()) {
            return "/admin/category/update";
        }

        this.categoryService.updateCategory(updateCategoryDto);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật loại sản phẩm thành công");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/delete/{categoryId}")
    public String showDeleteCategoryPage(
            @PathVariable Long categoryId,
            Model model
    )  {
        model.addAttribute("categoryId", categoryId);

        return "/admin/category/delete";
    }

    @PostMapping("/delete")
    public String deleteCategory(
            Long categoryId,
            RedirectAttributes redirectAttributes
    )  {
        this.categoryService.deleteCategory(categoryId);

        redirectAttributes.addFlashAttribute("successMessage", "Xóa loại sản phẩm thành công");

        return "redirect:/dashboard/category";
    }

    @GetMapping("/details/{categoryId}")
    public String showDetailsCategoryPage(
            @PathVariable Long categoryId,
            Model model
    )  {
        Category category = this.categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);

        return "/admin/category/details";
    }
}
