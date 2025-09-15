package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.validation.product.UpdateProductConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@UpdateProductConstraint
public class UpdateProductDto extends BaseProductDto {
    private Long id;
    private String oldName;
    private List<MultipartFile> newImages;
    private List<String> currentImageNames;
    private List<String> deleteImageNames;
    private ProductMetaDataDto productMetaData;

    public UpdateProductDto(
            Long id,
            String name,
            String oldName,
            double price,
            float discount,
            long quantity,
            String shortDescription,
            String description,
            Category category,
            Brand brand,

            String cpu,
            String gpu,
            String ram,
            String storage,
            String io,
            String screen,
            String keyboard,
            String audio,
            String sdCard,
            String lan,
            String wifi,
            String bluetooth,
            String webCam,
            String os,
            String battery,
            String weight,
            String color,
            String size,
            String cooling,
            String material,

            ProductMetaDataDto productMetaData,
            List<String> currentImageNames
    ) {
        this.id = id;
        this.oldName = oldName;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.brand = brand;

        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.storage = storage;
        this.io = io;
        this.screen = screen;
        this.keyboard = keyboard;
        this.audio = audio;
        this.sdCard = sdCard;
        this.lan = lan;
        this.wifi = wifi;
        this.bluetooth = bluetooth;
        this.webCam = webCam;
        this.os = os;
        this.battery = battery;
        this.weight = weight;
        this.color = color;
        this.size = size;
        this.cooling = cooling;
        this.material = material;

        this.productMetaData = productMetaData;
        this.currentImageNames = currentImageNames;
    }
}
