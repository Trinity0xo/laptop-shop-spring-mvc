package com.laptopstore.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.laptopstore.ecommerce.dto.product.CreateProductDto;
import com.laptopstore.ecommerce.dto.product.ProductCriteriaDto;
import com.laptopstore.ecommerce.dto.product.UpdateProductDto;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.repository.ProductRepository;
import com.laptopstore.ecommerce.specification.ProductSpecification;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PageableService pageableService;

    public ProductService(ProductRepository productRepository, PageableService pageableService) {
        this.productRepository = productRepository;
        this.pageableService = pageableService;
    }

    public Product handleCreateProduct(CreateProductDto createProductDto) {
        Product product = new Product();

        product.setName(createProductDto.getName());
        product.setBrand(createProductDto.getBrand());
        product.setPrice(createProductDto.getPrice());
        product.setDiscount(createProductDto.getDiscount());
        product.setQuantity(createProductDto.getQuantity());
        product.setShortDescription(createProductDto.getShortDescription());
        product.setDescription(createProductDto.getDescription());
        product.setCategory(createProductDto.getCategory());

        return this.productRepository.save(product);
    }

    public void handleUpdateProduct(Product product){
        this.productRepository.save(product);
    }

    public void handleUpdateProduct(UpdateProductDto updateProductDto, Product product) {
        product.setName(updateProductDto.getName());
        product.setBrand(updateProductDto.getBrand());
        product.setPrice(updateProductDto.getPrice());
        product.setDiscount(updateProductDto.getDiscount());
        product.setQuantity(updateProductDto.getQuantity());
        product.setShortDescription(updateProductDto.getShortDescription());
        product.setDescription(updateProductDto.getDescription());
        product.setCategory(updateProductDto.getCategory());

        this.productRepository.save(product);
    }

    public void handleDeleteProductById(Long id) {
        this.productRepository.deleteById(id);
    }

    public Product handleGetProductById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public Page<Product> handleGetAllProducts(ProductCriteriaDto productCriteriaDto) {
        Specification<Product> specification = Specification.where(null);

        Pageable pageable = pageableService.handleCreatePageable(productCriteriaDto.getIntegerPage(),
                productCriteriaDto.getIntegerLimit(), productCriteriaDto.getSortBy(),
                productCriteriaDto.getEnumSortDirection());

        if (productCriteriaDto.getName() != null && !productCriteriaDto.getName().isEmpty()) {
            Specification<Product> currentSpecification = ProductSpecification.nameLike(productCriteriaDto.getName());
            specification = specification.and(currentSpecification);
        }

        if (productCriteriaDto.getCategoryNames() != null && !productCriteriaDto.getCategoryNames().isEmpty()) {
            Specification<Product> currentSpecification = ProductSpecification
                    .categoryIn(productCriteriaDto.getCategoryNames());
            specification = specification.and(currentSpecification);
        }

        if (productCriteriaDto.getBrandNames() != null && !productCriteriaDto.getBrandNames().isEmpty()) {
            Specification<Product> currentSpecification = ProductSpecification
                    .brandIn(productCriteriaDto.getBrandNames());
            specification = specification.and(currentSpecification);
        }

        if (productCriteriaDto.getDoubleMinPrice() != null && productCriteriaDto.getDoubleMinPrice() > 0
                && productCriteriaDto.getDoubleMaxPrice() != null && productCriteriaDto.getDoubleMaxPrice() > 0
                && productCriteriaDto.getDoubleMinPrice() <= productCriteriaDto.getDoubleMaxPrice()) {
            Specification<Product> currentSpecification = ProductSpecification
                    .priceBetween(productCriteriaDto.getDoubleMinPrice(), productCriteriaDto.getDoubleMaxPrice());
            specification = specification.and(currentSpecification);
        }

        if (productCriteriaDto.getDoubleMinDiscountPrice() != null && productCriteriaDto.getDoubleMinDiscountPrice() > 0
                && productCriteriaDto.getDoubleMaxDiscountPrice() != null && productCriteriaDto.getDoubleMaxDiscountPrice() > 0
                && productCriteriaDto.getDoubleMinDiscountPrice() <= productCriteriaDto.getDoubleMaxDiscountPrice()) {
            Specification<Product> currentSpecification = ProductSpecification
                    .discountPriceBetween(productCriteriaDto.getDoubleMinDiscountPrice(), productCriteriaDto.getDoubleMaxDiscountPrice());
            specification = specification.and(currentSpecification);
        }

//        if (productCriteriaDto.getBooleanStockStatus() != null) {
//            Specification<Product> currentSpecification = ProductSpecification.stockStatus(productCriteriaDto.getBooleanStockStatus());
//            specification = specification.and(currentSpecification);
//        }

        return this.productRepository.findAll(specification, pageable);
    }

}
