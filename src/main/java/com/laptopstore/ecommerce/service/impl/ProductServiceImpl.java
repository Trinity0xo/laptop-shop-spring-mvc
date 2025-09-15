package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.dto.product.*;
import com.laptopstore.ecommerce.dto.product.CreateProductDto;
import com.laptopstore.ecommerce.dto.product.UpdateProductDto;
import com.laptopstore.ecommerce.dto.response.PageResponse;
import com.laptopstore.ecommerce.model.*;
import com.laptopstore.ecommerce.repository.*;
import com.laptopstore.ecommerce.service.FileService;
import com.laptopstore.ecommerce.service.FolderService;
import com.laptopstore.ecommerce.service.ProductImageService;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.specification.ReviewSpecifications;
import com.laptopstore.ecommerce.util.PaginationUtils;
import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;
import com.laptopstore.ecommerce.exception.AuthUserNotFoundException;
import com.laptopstore.ecommerce.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laptopstore.ecommerce.util.RatingUtils.roundToNearestHalf;
import static com.laptopstore.ecommerce.util.SlugUtils.toSlug;

@Service
public class ProductServiceImpl implements ProductService {
    private final OrderItemsRepository orderItemsRepository;
    private final ReviewRepository reviewRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ProductImageService productImageService;
    private final UserRepository userRepository;
    private final FolderService folderService;

    public ProductServiceImpl(OrderItemsRepository orderItemsRepository, ReviewRepository reviewRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, ProductRepository productRepository, FileServiceImpl fileService, ProductImageServiceImpl productImageService, UserRepository userRepository, FolderServiceImpl folderService) {
        this.orderItemsRepository = orderItemsRepository;
        this.reviewRepository = reviewRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.productImageService = productImageService;
        this.userRepository = userRepository;
        this.folderService = folderService;
    }

    @Override
    public Product getProductById(long productId) {
        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException(productId);
        }

        return product;
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findByName(name).orElse(null);
    }

    @Override
    public Product getProductBySlug(String slug) {
        Product product = this.productRepository.findBySlug(slug).orElse(null);
        if(product == null){
            throw new ProductNotFoundException(slug);
        }

        return product;
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductSoldDto>> getTopSellingProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit()
        );

        Page<CustomProductSoldDto> topSellingProducts = this.productRepository.findTopSellingProducts(productFilterDto, pageable);

        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductSoldDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, topSellingProducts.getContent()
        );

        return new PageResponse<>(
                topSellingProducts.getPageable().getPageNumber() + 1,
                topSellingProducts.getTotalPages(),
                topSellingProducts.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductRatingDto>> getTopRatedProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit()
        );

        Page<CustomProductRatingDto> topRatedProducts = this.productRepository.findTopRatedProducts(productFilterDto, pageable);

        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductRatingDto> customProductListDto = new CustomProductListDto<>(
            categories, brands, topRatedProducts.getContent()
        );

        return new PageResponse<>(
                topRatedProducts.getPageable().getPageNumber() + 1,
                topRatedProducts.getTotalPages(),
                topRatedProducts.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductStockDto>> getLowStockProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomProductStockDto.VALID_SORT_FIELDS, productFilterDto.getSortBy(), CustomProductStockDto.DEFAULT_SORT_FIELD),
                productFilterDto.getEnumSortDirection()
        );

        Page<CustomProductStockDto> products = this.productRepository.findLowStockProducts(productFilterDto, pageable);
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductStockDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, products.getContent()
        );

        return new PageResponse<>(
                products.getPageable().getPageNumber() + 1,
                products.getTotalPages(),
                products.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductDiscountDto>> getDiscountProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomProductDiscountDto.VALID_SORT_FIELDS, productFilterDto.getSortBy(), CustomProductDiscountDto.DEFAULT_SORT_FIELD),
                productFilterDto.getEnumSortDirection()
        );

        Page<CustomProductDiscountDto> products = this.productRepository.findDiscountProducts(productFilterDto, pageable);
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductDiscountDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, products.getContent()
        );

        return new PageResponse<>(
                products.getPageable().getPageNumber() + 1,
                products.getTotalPages(),
                products.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductDto>> getTopDiscountProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit()
        );

        Page<CustomProductDto> products = this.productRepository.findTopDiscountProducts(productFilterDto, pageable);
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, products.getContent()
        );

        return new PageResponse<>(
                products.getPageable().getPageNumber() + 1,
                products.getTotalPages(),
                products.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductDto>> getShopProducts(ProductFilterDto productFilterDto) {
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomProductDto.VALID_SORT_FIELDS, productFilterDto.getSortBy(), CustomProductDto.DEFAULT_SORT_FIELD),
                productFilterDto.getEnumSortDirection()
        );

        Page<CustomProductDto> products = this.productRepository.findShopProducts(productFilterDto, pageable);
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, products.getContent()
        );

        return new PageResponse<>(
                products.getPageable().getPageNumber() + 1,
                products.getTotalPages(),
                products.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public CustomProductDetailsDto getShopProductDetailsBySlug(String productSlug){
        Product product = this.getProductBySlug(productSlug);

        Double averageRating = this.reviewRepository.findAverageRatingByProduct(product);
        averageRating = (averageRating == null) ? 0D : roundToNearestHalf(averageRating);

        long totalRatings = this.reviewRepository.countByProduct(product);

        // n + 1 (not good)
        Map<Integer, Integer> ratingPercentagesMap = new HashMap<>();
        Map<Integer, Long> ratingCountMap = new HashMap<>();

        for (int i = 0;i<=5;i++){
            long ratingCount = this.reviewRepository.countByProductAndRating(product, i);
            ratingCountMap.put(i, ratingCount);

            Double ratingPercentage = this.reviewRepository.findAverageRatingByProductAndRating(product,i);
            if(ratingPercentage == null){
                ratingPercentagesMap.put(i, 0);
            }else{
                ratingPercentagesMap.put(i,(int) Math.round(ratingPercentage));
            }
        }

        Specification<Review> specification = Specification.where(ReviewSpecifications.equalProductId(product.getId()));
        Pageable pageable = PaginationUtils.createPageable(10);

        List<Review> userReviews = this.reviewRepository.findAll(specification, pageable).getContent();
        List<CustomProductDto> relatedProducts = this.productRepository.findRelatedProducts(product, pageable).getContent();

        return new CustomProductDetailsDto(
                product.getId(),
                product.getProductImages(),
                product.getName(),
                product.getPrice(),
                product.getDiscount(),
                product.getDiscountPrice(),
                product.getQuantity(),
                product.getShortDescription(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getSlug(),

                product.getCpu(),
                product.getGpu(),
                product.getRam(),
                product.getStorage(),
                product.getIo(),
                product.getScreen(),
                product.getKeyboard(),
                product.getAudio(),
                product.getSdCard(),
                product.getLan(),
                product.getWifi(),
                product.getBluetooth(),
                product.getWebCam(),
                product.getOs(),
                product.getBattery(),
                product.getWeight(),
                product.getColor(),
                product.getSize(),
                product.getCooling(),
                product.getMaterial(),

                totalRatings,
                averageRating,
                ratingPercentagesMap,
                ratingCountMap,
                userReviews,
                relatedProducts
        );
    }

    @Override
    public CustomProductDetailsDto getShopProductDetailsBySlug(String productSlug, String email){
        User user = this.userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new AuthUserNotFoundException(email);
        }

        Product product = this.getProductBySlug(productSlug);

        boolean purchased = this.orderItemsRepository.existsByOrderUserAndOrderStatusAndProduct(user, OrderStatusEnum.DELIVERED, product);

        Review myReview = this.reviewRepository.findByUserAndProduct(user, product).orElse(null);

        Double averageRating = this.reviewRepository.findAverageRatingByProduct(product);
        averageRating = (averageRating == null) ? 0D : roundToNearestHalf(averageRating);

        long totalRatings = this.reviewRepository.countByProduct(product);

        Map<Integer, Integer> ratingPercentagesMap = new HashMap<>();
        Map<Integer, Long> ratingCountMap = new HashMap<>();

        for (int i = 0;i<=5;i++){
            long ratingCount = this.reviewRepository.countByProductAndRating(product, i);
            ratingCountMap.put(i, ratingCount);

            Double ratingPercentage = this.reviewRepository.findAverageRatingByProductAndRating(product,i);
            if(ratingPercentage == null){
                ratingPercentagesMap.put(i, 0);
            }else{
                ratingPercentagesMap.put(i,(int) Math.round(ratingPercentage));
            }
        }

        Specification<Review> specification = Specification.where(ReviewSpecifications.equalProductId(product.getId()))
                .and(ReviewSpecifications.notEqualUserId(user.getId()));
        Pageable pageable = PaginationUtils.createPageable(10);

        List<Review> userReviews = this.reviewRepository.findAll(specification, pageable).getContent();
        List<CustomProductDto> relatedProducts = this.productRepository.findRelatedProducts(product, pageable).getContent();

        return new CustomProductDetailsDto(
                product.getId(),
                product.getProductImages(),
                product.getName(),
                product.getPrice(),
                product.getDiscount(),
                product.getDiscountPrice(),
                product.getQuantity(),
                product.getShortDescription(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getSlug(),

                product.getCpu(),
                product.getGpu(),
                product.getRam(),
                product.getStorage(),
                product.getIo(),
                product.getScreen(),
                product.getKeyboard(),
                product.getAudio(),
                product.getSdCard(),
                product.getLan(),
                product.getWifi(),
                product.getBluetooth(),
                product.getWebCam(),
                product.getOs(),
                product.getBattery(),
                product.getWeight(),
                product.getColor(),
                product.getSize(),
                product.getCooling(),
                product.getMaterial(),

                totalRatings,
                averageRating,
                ratingPercentagesMap,
                ratingCountMap,
                userReviews,
                myReview,
                purchased,
                relatedProducts
        );
    }

    @Override
    public PageResponse<CustomProductListDto<CustomProductDto>> getAdminProducts(ProductFilterDto productFilterDto){
        Pageable pageable = PaginationUtils.createPageable(
                productFilterDto.getIntegerPage(),
                productFilterDto.getIntegerLimit(),
                PaginationUtils.getValidSortBy(CustomProductDto.class, productFilterDto.getSortBy(), CustomProductDto.DEFAULT_SORT_FIELD),
                productFilterDto.getEnumSortDirection()
        );

        Page<CustomProductDto> products = this.productRepository.findAllProducts(productFilterDto, pageable);
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        CustomProductListDto<CustomProductDto> customProductListDto = new CustomProductListDto<>(
                categories, brands, products.getContent()
        );

        return new PageResponse<>(
                products.getPageable().getPageNumber() + 1,
                products.getTotalPages(),
                products.getTotalElements(),
                customProductListDto
        );
    }

    @Override
    public CreateProductDto getInformationForCreateProduct(){
        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        ProductMetaDataDto productMetaData = new ProductMetaDataDto(categories, brands);

        return new CreateProductDto(productMetaData);
    }

    @Override
    public void createProduct(CreateProductDto createProductDto){
        Product product = new Product(
                createProductDto.getName(),
                createProductDto.getPrice(),
                createProductDto.getDiscount(),
                createProductDto.getQuantity(),
                createProductDto.getShortDescription(),
                createProductDto.getDescription(),
                createProductDto.getCategory(),
                createProductDto.getBrand(),
                toSlug(createProductDto.getName()),

                createProductDto.getCpu(),
                createProductDto.getGpu(),
                createProductDto.getRam(),
                createProductDto.getStorage(),
                createProductDto.getIo(),
                createProductDto.getScreen(),
                createProductDto.getKeyboard(),
                createProductDto.getAudio(),
                createProductDto.getSdCard(),
                createProductDto.getLan(),
                createProductDto.getWifi(),
                createProductDto.getBluetooth(),
                createProductDto.getWebCam(),
                createProductDto.getOs(),
                createProductDto.getBattery(),
                createProductDto.getWeight(),
                createProductDto.getColor(),
                createProductDto.getSize(),
                createProductDto.getCooling(),
                createProductDto.getMaterial()
        );

        product = this.productRepository.save(product);

        if(createProductDto.getNewImages() != null && !createProductDto.getNewImages().isEmpty()) {
            List<String> imageNames = new ArrayList<>();
            String productImagesFolderName = this.folderService.getProductImagesFolderName();

            for (MultipartFile image : createProductDto.getNewImages()) {
                String imageName = this.fileService.uploadFile(image, productImagesFolderName);
                if (imageName != null) {
                    imageNames.add(imageName);
                }
            }

            this.productImageService.createProductImages(imageNames, product, false);
        }
    }

    @Override
    public void deleteProduct(long productId){
        Product product = this.productRepository.findById(productId).orElse(null);
        if(product != null){
            List<ProductImage> productImages = product.getProductImages();

            if(productImages != null && !productImages.isEmpty()){
                String productImagesFolderName = this.folderService.getProductImagesFolderName();

                for (ProductImage productImage : productImages){
                    this.fileService.deleteFile(productImage.getImageName(), productImagesFolderName);
                    this.productImageService.deleteProductImageById(productImage.getId());
                }
            }

            this.productRepository.delete(product);
        }
    }

    @Override
    public UpdateProductDto getInformationForUpdateProduct(long productId){
        Product product = this.getProductById(productId);

        List<String> currentImageNames = new ArrayList<>();

        if(product.getProductImages() != null && !product.getProductImages().isEmpty()){
            for(ProductImage productImage : product.getProductImages()){
                currentImageNames.add(productImage.getImageName());
            }
        }

        List<Category> categories = this.categoryRepository.findAll();
        List<Brand> brands = this.brandRepository.findAll();

        ProductMetaDataDto productMetaData = new ProductMetaDataDto(categories, brands);

        return new UpdateProductDto(
                product.getId(),
                product.getName(),
                product.getName(),
                product.getPrice(),
                product.getDiscount(),
                product.getQuantity(),
                product.getShortDescription(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),

                product.getCpu(),
                product.getGpu(),
                product.getRam(),
                product.getStorage(),
                product.getIo(),
                product.getScreen(),
                product.getKeyboard(),
                product.getAudio(),
                product.getSdCard(),
                product.getLan(),
                product.getWifi(),
                product.getBluetooth(),
                product.getWebCam(),
                product.getOs(),
                product.getBattery(),
                product.getWeight(),
                product.getColor(),
                product.getSize(),
                product.getCooling(),
                product.getMaterial(),

                productMetaData,
                currentImageNames
        );
    }

    @Override
    public void updateProduct(UpdateProductDto updateProductDto){
        Product product = this.getProductById(updateProductDto.getId());

        product.setName(updateProductDto.getName());
        product.setSlug(toSlug(updateProductDto.getName()));
        product.setBrand(updateProductDto.getBrand());
        product.setPrice(updateProductDto.getPrice());
        product.setDiscount(updateProductDto.getDiscount());
        product.setQuantity(updateProductDto.getQuantity());
        product.setShortDescription(updateProductDto.getShortDescription());
        product.setDescription(updateProductDto.getDescription());
        product.setCategory(updateProductDto.getCategory());

        product.setCpu(updateProductDto.getCpu());
        product.setGpu(updateProductDto.getGpu());
        product.setRam(updateProductDto.getRam());
        product.setAudio(updateProductDto.getAudio());
        product.setBattery(updateProductDto.getBattery());
        product.setIo(updateProductDto.getIo());
        product.setBluetooth(updateProductDto.getBluetooth());
        product.setColor(updateProductDto.getColor());
        product.setOs(updateProductDto.getOs());
        product.setKeyboard(updateProductDto.getKeyboard());
        product.setLan(updateProductDto.getLan());
        product.setScreen(updateProductDto.getScreen());
        product.setSdCard(updateProductDto.getSdCard());
        product.setWeight(updateProductDto.getWeight());
        product.setWifi(updateProductDto.getWifi());
        product.setSize(updateProductDto.getSize());
        product.setStorage(updateProductDto.getStorage());
        product.setWebCam(updateProductDto.getWebCam());
        product.setCooling(updateProductDto.getCooling());
        product.setMaterial(updateProductDto.getMaterial());

        String productImagesFolderName = this.folderService.getProductImagesFolderName();

        boolean mainDeleted = false;

        if (updateProductDto.getDeleteImageNames() != null && !updateProductDto.getDeleteImageNames().isEmpty()) {
            ProductImage mainImage = this.productImageService.getProductMainImageById(product.getId());
            if (mainImage != null && updateProductDto.getDeleteImageNames().contains(mainImage.getImageName())) {
                mainDeleted = true;
            }

            for (String imageName : updateProductDto.getDeleteImageNames()) {
                this.fileService.deleteFile(imageName, productImagesFolderName);
                ProductImage productImage = this.productImageService.getProductImageByName(imageName);
                if (productImage != null) {
                    this.productImageService.deleteProductImageById(productImage.getId());
                }
            }
        }

        if (updateProductDto.getNewImages() != null && !updateProductDto.getNewImages().isEmpty()) {
            List<String> imageNames = new ArrayList<>();
            for (MultipartFile image : updateProductDto.getNewImages()) {
                String imageName = this.fileService.uploadFile(image, productImagesFolderName);
                if (imageName != null) {
                    imageNames.add(imageName);
                }
            }

            this.productImageService.createProductImages(imageNames, product, !mainDeleted);
        } else if (mainDeleted) {
            ProductImage anotherImage = this.productImageService.getAnyImageByProductId(product.getId());
            if (anotherImage != null) {
                anotherImage.setMain(true);
                this.productImageService.saveProductImage(anotherImage);
            }
        }
    }

    @Override
    public CustomProductDetailsDto getAdminProductDetailsById(long productId){
        Product product = this.productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException(productId);
        }

       long sold = this.orderItemsRepository.countSoldProductByProductId(product.getId());

        return new CustomProductDetailsDto(
                product.getId(),
                product.getProductImages(),
                product.getName(),
                product.getPrice(),
                product.getDiscount(),
                product.getQuantity(),
                product.getShortDescription(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getSlug(),

                product.getCpu(),
                product.getGpu(),
                product.getRam(),
                product.getStorage(),
                product.getIo(),
                product.getScreen(),
                product.getKeyboard(),
                product.getAudio(),
                product.getSdCard(),
                product.getLan(),
                product.getWifi(),
                product.getBluetooth(),
                product.getWebCam(),
                product.getOs(),
                product.getBattery(),
                product.getWeight(),
                product.getColor(),
                product.getSize(),
                product.getCooling(),
                product.getMaterial(),

                sold,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
