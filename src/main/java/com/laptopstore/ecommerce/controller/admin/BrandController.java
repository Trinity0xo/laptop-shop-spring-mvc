package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.dto.brand.BrandFilterDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.model.Brand;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("")
    public String showBrandPage(
            Model model,
            BrandFilterDto brandFilterDto
    ) {
        PageResponse<List<Brand>> response = this.brandService.getAllBrands(brandFilterDto);
        model.addAttribute("response", response);
        model.addAttribute("brandFilterDto", brandFilterDto);

        return "/admin/brand/index";
    }

    @GetMapping("/create")
    public String showCreateBrandPage(
            Model model
    ) {
        model.addAttribute("createBrandDto", new CreateBrandDto());

        return "/admin/brand/create";
    }

    @PostMapping("/create")
    public String createBrand(
            @Valid CreateBrandDto createBrandDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "/admin/brand/create";
        }

        this.brandService.createBrand(createBrandDto);

        redirectAttributes.addFlashAttribute("successMessage", "Tạo thương hiệu thành công");

        return "redirect:/dashboard/brand";
    }

    @GetMapping("/update/{brandId}")
    public String showUpdatePage(
            @PathVariable long brandId,
            Model model
    ) {
        UpdateBrandDto updateBrandDto = this.brandService.getInformationForUpdateBrand(brandId);
        model.addAttribute("updateBrandDto", updateBrandDto);

        return "/admin/brand/update";
    }

    @PostMapping("/update")
    public String updateBrand(
            @Valid UpdateBrandDto updateBrandDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            UpdateBrandDto newUpdateBrandDto = this.brandService.getInformationForUpdateBrand(updateBrandDto.getId());
            updateBrandDto.setOldName(newUpdateBrandDto.getOldName());
            return "/admin/brand/update";
        }

        this.brandService.updateBrand(updateBrandDto);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thương hiệu thành công");

        return "redirect:/dashboard/brand";
    }

    @GetMapping("/delete/{brandId}")
    public String showDeleteBrandPage(
            @PathVariable long brandId,
            Model model
    ) {
        Brand brand = this.brandService.getBrandById(brandId);
        model.addAttribute("brand", brand);

        return "/admin/brand/delete";
    }

    @PostMapping("/delete")
    public String deleteBrand(
            @RequestParam long brandId,
            RedirectAttributes redirectAttributes
    ) {
        this.brandService.deleteBrand(brandId);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thương hiệu thành công");

        return "redirect:/dashboard/brand";
    }

    @GetMapping("/details/{brandId}")
    public String showBrandDetailsPage(
            @PathVariable long brandId,
            Model model
    ) {
        Brand brand = this.brandService.getBrandById(brandId);
        model.addAttribute("brand", brand);

        return "/admin/brand/details";
    }
}
