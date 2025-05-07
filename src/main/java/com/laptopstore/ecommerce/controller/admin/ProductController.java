package com.laptopstore.ecommerce.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laptopstore.ecommerce.dto.response.AjaxResponse;
import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.service.*;
import com.laptopstore.ecommerce.util.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.laptopstore.ecommerce.dto.product.CreateProductDto;
import com.laptopstore.ecommerce.dto.product.ProductCriteriaDto;
import com.laptopstore.ecommerce.dto.product.UpdateProductDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.model.Product;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/dashboard/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final FileService fileService;
    private final ProductImageService productImageService;

    public ProductController(ProductService productService, CategoryService categoryService,
            BrandService brandService, FileService fileService, ProductImageService productImageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.fileService = fileService;
        this.productImageService = productImageService;
    }

    @GetMapping("")
    public String showProductPage(
            ProductCriteriaDto productCriteriaDto,
            Model model
    ) throws Exception {
        Page<Product> products = this.productService.handleGetAllProducts(productCriteriaDto);
        List<Category> categories = this.categoryService.handleGetAllCategories();
        List<Brand> brands = this.brandService.handleGetAllBrands();
        model.addAttribute("productList", products.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getPageable().getPageNumber() + 1);
        model.addAttribute("resultCount", products.getTotalElements());
        model.addAttribute("query", productCriteriaDto);

        return "/admin/product/index";
    }

    @GetMapping("/create")
    public String showCreateProductPage(
            Model model
    ) throws Exception {
        List<Category> categories = this.categoryService.handleGetAllCategories();
        List<Brand> brands = this.brandService.handleGetAllBrands();
        model.addAttribute("createProductDto", new CreateProductDto());
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        return "/admin/product/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>> createProduct(
            @Valid CreateProductDto createProductDto,
            BindingResult bindingResult) {

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            ajaxResponse.setMessage("validation error");
            ajaxResponse.setData(errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponse);
        }

        Product product = this.productService.handleCreateProduct(createProductDto);

        if(createProductDto.getImages() != null && !createProductDto.getImages().isEmpty()) {
            List<String> imageNames = new ArrayList<>();

            for (MultipartFile image : createProductDto.getImages()) {
                String imageName = this.fileService.handleUploadFile(image, "product_pictures");
                if (imageName != null) {
                    imageNames.add(imageName);
                }
            }

            for (String imageName : imageNames) {
                this.productImageService.handleCreateProductImage(imageName, product);
            }
        }

        ajaxResponse.setMessage("Create new product success");

        return ResponseEntity.status(HttpStatus.CREATED).body(ajaxResponse);
    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Product product = this.productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setId(product.getId());
        updateProductDto.setName(product.getName());
        updateProductDto.setBrand(product.getBrand());
        updateProductDto.setPrice(product.getPrice());
        updateProductDto.setDiscount(product.getDiscount());
        updateProductDto.setQuantity(product.getQuantity());
        updateProductDto.setShortDescription(product.getShortDescription());
        updateProductDto.setDescription(product.getDescription());
        updateProductDto.setCategory(product.getCategory());

        List<Category> categories = this.categoryService.handleGetAllCategories();
        List<Brand> brands = this.brandService.handleGetAllBrands();

        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("updateProductDto", updateProductDto);
        model.addAttribute("productImages", product.getProductImages());

        return "/admin/product/edit";
    }

    @PostMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<AjaxResponse<Object>> updateProduct(
            @PathVariable Long id,
            @Valid UpdateProductDto updateProductDto,
            BindingResult bindingResult
    )
            throws Exception {

        AjaxResponse<Object> ajaxResponse = new AjaxResponse<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            ajaxResponse.setMessage("validation errors");
            ajaxResponse.setData(errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponse);
        }

        Product product = this.productService.handleGetProductById(updateProductDto.getId());

        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        this.productService.handleUpdateProduct(updateProductDto, product);

        if(updateProductDto.getImages() != null && !updateProductDto.getImages().isEmpty()) {
            List<String> imageNames = new ArrayList<>();

            for (MultipartFile image : updateProductDto.getImages()) {
                String imageName = this.fileService.handleUploadFile(image, "product_pictures");
                if (imageName != null) {
                    imageNames.add(imageName);
                }
            }

            for (String imageName : imageNames) {
                this.productImageService.handleCreateProductImage(imageName, product);
            }
        }

        if(updateProductDto.getDeleteImageNames() != null && !updateProductDto.getDeleteImageNames().isEmpty()) {
            for(String imageName : updateProductDto.getDeleteImageNames()) {
                this.fileService.handleDeleteFile(imageName, "product_pictures");
                ProductImage productImage = this.productImageService.handleGetProductImageByName(imageName);
                if(productImage != null) {
                    this.productImageService.handleDeleteProductImageById(productImage.getId());
                }
            }
        }

        ajaxResponse.setMessage("Update product success");

        return ResponseEntity.status(HttpStatus.OK).body(ajaxResponse);
    }

    @GetMapping("/delete/{id}")
    public String showDeleteProductPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Product product = this.productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        model.addAttribute("product", product);

        return "/admin/product/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id
    ) throws Exception {
        Product product = this.productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        if(product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            for(ProductImage productImage : product.getProductImages()){
                this.fileService.handleDeleteFile(productImage.getImageName(), "product_pictures");
                this.productImageService.handleDeleteProductImageById(productImage.getId());
            }
        }

        this.productService.handleDeleteProductById(id);

        String successMessage = "Delete product success";

        return "redirect:/dashboard/product?successMessage=" + successMessage;
    }

    @GetMapping("/details/{id}")
    public String showDetailsProductPage(
            @PathVariable Long id,
            Model model
    ) throws Exception {
        Product product = this.productService.handleGetProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        model.addAttribute("product", product);

        return "/admin/product/details";
    }
}
