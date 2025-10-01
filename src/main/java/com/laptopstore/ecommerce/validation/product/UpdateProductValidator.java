package com.laptopstore.ecommerce.validation.product;

import com.laptopstore.ecommerce.dto.product.UpdateProductDto;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.service.ProductImageService;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.util.ImageUtils;
import com.laptopstore.ecommerce.util.SlugUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.laptopstore.ecommerce.util.ImageUtils.MAX_IMAGES_SIZE;

public class UpdateProductValidator extends BaseProductValidator<UpdateProductDto> implements ConstraintValidator<UpdateProductConstraint, UpdateProductDto> {
    private final ProductImageService productImageService;
    private final ProductService productService;

    public UpdateProductValidator(ProductImageService productImageService, ProductService productService) {

        this.productImageService = productImageService;
        this.productService = productService;
    }

    @Override
    public boolean isValid(UpdateProductDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value, context);

        Product exists = this.productService.getProductBySlug(SlugUtils.toSlug(value.getName()));
        if(exists != null && !exists.getId().equals(value.getId())){
            context.buildConstraintViolationWithTemplate("Sản phẩm với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        List<ProductImage> productImages = this.productImageService.getProductImagesByProductId(value.getId());

        if(value.getDeleteImageNames() != null && !value.getDeleteImageNames().isEmpty() && (value.getNewImages() == null || value.getNewImages().isEmpty())){
            if(value.getDeleteImageNames().size() >= productImages.size()){
                context.buildConstraintViolationWithTemplate("Sản phẩm phải có ít nhất 1 ảnh")
                    .addPropertyNode("newImages")
                    .addConstraintViolation();
                isValid = false;
            }
        }

        if(value.getDeleteImageNames() != null && !value.getDeleteImageNames().isEmpty() && value.getNewImages() != null && !value.getNewImages().isEmpty()) {
            for(MultipartFile image : value.getNewImages()) {
                if(!ImageUtils.isValidImageType(image.getContentType())) {
                    context.buildConstraintViolationWithTemplate("File ảnh không hợp lệ")
                            .addPropertyNode("newImages")
                            .addConstraintViolation();
                    isValid = false;
                }

                int imagesLengthAfterDelete = productImages.size() - value.getDeleteImageNames().size();

                if(imagesLengthAfterDelete >= 0){
                    int imagesLengthAfterAdd = imagesLengthAfterDelete + value.getNewImages().size();

                    if(imagesLengthAfterAdd > MAX_IMAGES_SIZE){
                        context.buildConstraintViolationWithTemplate("Bạn chỉ có thể upload tối đa " + MAX_IMAGES_SIZE + " ảnh")
                                .addPropertyNode("newImages")
                                .addConstraintViolation();
                        isValid = false;
                    }
                }
            }
        }

        if((value.getDeleteImageNames() == null || value.getDeleteImageNames().isEmpty()) && value.getNewImages() != null && !value.getNewImages().isEmpty()){
            for(MultipartFile image : value.getNewImages()) {
                if(!ImageUtils.isValidImageType(image.getContentType())) {
                    context.buildConstraintViolationWithTemplate("File ảnh không hợp lệ")
                            .addPropertyNode("newImages")
                            .addConstraintViolation();
                    isValid = false;
                }

                int imagesLengthAfterAdd = productImages.size() + value.getNewImages().size();

                if(imagesLengthAfterAdd > MAX_IMAGES_SIZE){
                    context.buildConstraintViolationWithTemplate("Bạn chỉ có thể upload tối đa " + MAX_IMAGES_SIZE + " ảnh")
                            .addPropertyNode("newImages")
                            .addConstraintViolation();
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    @Override
    protected String getName(UpdateProductDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(UpdateProductDto dto) {
        return dto.getDescription();
    }

    @Override
    protected String getShortDescription(UpdateProductDto dto) {
        return dto.getShortDescription();
    }

    @Override
    protected String getCpu(UpdateProductDto dto) {
        return dto.getCpu();
    }

    @Override
    protected String getGpu(UpdateProductDto dto) {
        return dto.getGpu();
    }

    @Override
    protected String getRam(UpdateProductDto dto) {
        return dto.getRam();
    }

    @Override
    protected String getStorage(UpdateProductDto dto) {
        return dto.getStorage();
    }

    @Override
    protected String getScreen(UpdateProductDto dto) {
        return dto.getScreen();
    }

    @Override
    protected String getKeyboard(UpdateProductDto dto) {
        return dto.getKeyboard();
    }

    @Override
    protected String getSdCard(UpdateProductDto dto) {
        return dto.getSdCard();
    }

    @Override
    protected String getLan(UpdateProductDto dto) {
        return dto.getLan();
    }

    @Override
    protected String getWifi(UpdateProductDto dto) {
        return dto.getWifi();
    }

    @Override
    protected String getBluetooth(UpdateProductDto dto) {
        return dto.getBluetooth();
    }

    @Override
    protected String getWebCam(UpdateProductDto dto) {
        return dto.getWebCam();
    }

    @Override
    protected String getOs(UpdateProductDto dto) {
        return dto.getOs();
    }

    @Override
    protected String getBattery(UpdateProductDto dto) {
        return dto.getBattery();
    }

    @Override
    protected String getWeight(UpdateProductDto dto) {
        return dto.getWeight();
    }

    @Override
    protected String getColor(UpdateProductDto dto) {
        return dto.getColor();
    }

    @Override
    protected String getSize(UpdateProductDto dto) {
        return dto.getSize();
    }

    @Override
    protected String getCooling(UpdateProductDto dto) {
        return dto.getCooling();
    }

    @Override
    protected String getMaterial(UpdateProductDto dto) {
        return dto.getMaterial();
    }

    @Override
    protected String getIo(UpdateProductDto dto) {
        return dto.getIo();
    }

    @Override
    protected String getAudio(UpdateProductDto dto) {
        return dto.getAudio();
    }

    @Override
    protected Double getPrice(UpdateProductDto dto) {
        return dto.getPrice();
    }

    @Override
    protected Long getQuantity(UpdateProductDto dto) {
        return dto.getQuantity();
    }

    @Override
    protected Float getDiscount(UpdateProductDto dto) {
        return dto.getDiscount();
    }

}
