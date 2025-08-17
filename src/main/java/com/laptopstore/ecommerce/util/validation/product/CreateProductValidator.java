package com.laptopstore.ecommerce.util.validation.product;

import com.laptopstore.ecommerce.dto.product.CreateProductDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Product;
import com.laptopstore.ecommerce.service.ProductService;
import com.laptopstore.ecommerce.util.ImageUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static com.laptopstore.ecommerce.util.ImageUtils.MAX_IMAGES_SIZE;

public class CreateProductValidator extends BaseProductValidator<CreateProductDto> implements ConstraintValidator<CreateProductConstraint, CreateProductDto> {
    private final ProductService productService;

    public CreateProductValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected String getName(CreateProductDto dto) {
        return dto.getName();
    }

    @Override
    protected String getDescription(CreateProductDto dto) {
        return dto.getDescription();
    }

    @Override
    protected String getShortDescription(CreateProductDto dto) {
        return dto.getShortDescription();
    }

    @Override
    protected String getCpu(CreateProductDto dto) {
        return dto.getCpu();
    }

    @Override
    protected String getGpu(CreateProductDto dto) {
        return dto.getGpu();
    }

    @Override
    protected String getRam(CreateProductDto dto) {
        return dto.getRam();
    }

    @Override
    protected String getStorage(CreateProductDto dto) {
        return dto.getStorage();
    }

    @Override
    protected String getScreen(CreateProductDto dto) {
        return dto.getScreen();
    }

    @Override
    protected String getKeyboard(CreateProductDto dto) {
        return dto.getKeyboard();
    }

    @Override
    protected String getSdCard(CreateProductDto dto) {
        return dto.getSdCard();
    }

    @Override
    protected String getLan(CreateProductDto dto) {
        return dto.getLan();
    }

    @Override
    protected String getWifi(CreateProductDto dto) {
        return dto.getWifi();
    }

    @Override
    protected String getBluetooth(CreateProductDto dto) {
        return dto.getBluetooth();
    }

    @Override
    protected String getWebCam(CreateProductDto dto) {
        return dto.getWebCam();
    }

    @Override
    protected String getOs(CreateProductDto dto) {
        return dto.getOs();
    }

    @Override
    protected String getBattery(CreateProductDto dto) {
        return dto.getBattery();
    }

    @Override
    protected String getWeight(CreateProductDto dto) {
        return dto.getWeight();
    }

    @Override
    protected String getColor(CreateProductDto dto) {
        return dto.getColor();
    }

    @Override
    protected String getSize(CreateProductDto dto) {
        return dto.getSize();
    }

    @Override
    protected String getCooling(CreateProductDto dto) {
        return dto.getCooling();
    }

    @Override
    protected String getMaterial(CreateProductDto dto) {
        return dto.getMaterial();
    }

    @Override
    protected String getIo(CreateProductDto dto) {
        return dto.getIo();
    }

    @Override
    protected String getAudio(CreateProductDto dto) {
        return dto.getAudio();
    }

    @Override
    protected Double getPrice(CreateProductDto dto) {
        return dto.getPrice();
    }

    @Override
    protected Long getQuantity(CreateProductDto dto) {
        return dto.getQuantity();
    }

    @Override
    protected Float getDiscount(CreateProductDto dto) {
        return dto.getDiscount();
    }

    @Override
    public boolean isValid(CreateProductDto value, ConstraintValidatorContext context) {
        boolean isValid = validate(value, context);

        Product exists = this.productService.getProductByName(value.getName());
        if(exists != null){
            context.buildConstraintViolationWithTemplate("Sản phẩm với tên này đã tồn tại")
                    .addPropertyNode("name")
                    .addConstraintViolation();

            isValid = false;
        }

        if(value.getNewImages() == null || value.getNewImages().isEmpty()){
            context.buildConstraintViolationWithTemplate("Sản phẩm phải có ít nhất 1 ảnh")
                    .addPropertyNode("newImages")
                    .addConstraintViolation();
            isValid = false;
        }else {
            if (value.getNewImages().size() > MAX_IMAGES_SIZE) {
                context.buildConstraintViolationWithTemplate("Bạn chỉ có thể upload tối đa " + MAX_IMAGES_SIZE + " ảnh")
                        .addPropertyNode("newImages")
                        .addConstraintViolation();
                isValid = false;
            } else{
                for (MultipartFile image : value.getNewImages()) {
                    if (!ImageUtils.isValidImageType(image.getContentType())) {
                        context.buildConstraintViolationWithTemplate("File ảnh không hợp lệ")
                                .addPropertyNode("newImages")
                                .addConstraintViolation();
                        isValid = false;
                        break;
                    }
                }
            }
        }

        return isValid;
    }
}
