package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.laptopstore.ecommerce.dto.brand.BrandCriteriaDto;
import com.laptopstore.ecommerce.dto.brand.CreateBrandDto;
import com.laptopstore.ecommerce.dto.brand.UpdateBrandDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.service.BrandService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/dashboard/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("")
    public String showBrandPage(Model model, BrandCriteriaDto brandCriteriaDto) {
        Page<Brand> brands = this.brandService.handleGetAllBrands(brandCriteriaDto);
        model.addAttribute("brandList", brands.getContent());
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("currentPage", brands.getPageable().getPageNumber() + 1);
        model.addAttribute("query", brandCriteriaDto);
        model.addAttribute("resultCount", brands.getTotalElements());

        return "/admin/brand/index";
    }

    @GetMapping("/create")
    public String showCreateBrandPage(
            Model model) {

        model.addAttribute("createBrandDto", new CreateBrandDto());

        return "/admin/brand/create";
    }

    @PostMapping("/create")
    public String createBrand(
            @Valid CreateBrandDto createBrandDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/brand/create";
        }

        this.brandService.handleCreateBrand(createBrandDto);

        String successMessage = "Brand created successfully";

        return "redirect:/dashboard/brand?successMessage=" + successMessage;
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(
            @PathVariable Long id, Model model)
            throws Exception {

        Brand brand = brandService.handleGetBrandById(id);
        if (brand == null) {
            throw new NotFoundException("Brand not found");
        }

        UpdateBrandDto updateBrandDto = new UpdateBrandDto();
        updateBrandDto.setId(brand.getId());
        updateBrandDto.setName(brand.getName());
        updateBrandDto.setDescription(brand.getDescription());

        model.addAttribute("updateBrandDto", updateBrandDto);

        return "/admin/brand/edit";
    }

    @PostMapping("/edit/{id}")
    public String editBrand(
            @PathVariable Long id,
            @Valid UpdateBrandDto updateBrandDto,
            BindingResult bindingResult)
            throws Exception {

        if (bindingResult.hasErrors()) {
            return "/admin/brand/edit";
        }

        Brand brand = brandService.handleGetBrandById(id);
        if (brand == null) {
            throw new NotFoundException("Brand not found");
        }

        this.brandService.handleUpdateBrand(updateBrandDto, brand);

        String successMessage = "Brand updated successfully";

        return "redirect:/dashboard/brand?successMessage=" + successMessage;
    }

    @GetMapping("/delete/{id}")
    public String showDeleteBrandPage(@PathVariable Long id,
            Model model)
            throws Exception {
        Brand brand = brandService.handleGetBrandById(id);
        if (brand == null) {
            throw new NotFoundException("Brand not found");
        }

        model.addAttribute("brand", brand);
        return "/admin/brand/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        this.brandService.handleDeleteBrandById(id);

        String successMessage = "Brand deleted successfully";

        return "redirect:/dashboard/brand?successMessage=" + successMessage;
    }

    @GetMapping("/details/{id}")
    public String showDetailsPage(@PathVariable Long id, Model model){
        Brand brand = this.brandService.handleGetBrandById(id);
        if(brand == null){
            throw new NotFoundException("Brand not found");
        }

        model.addAttribute("brand", brand);

        return "/admin/brand/details";
    }
}
