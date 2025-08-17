package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class BaseProductDto {
    protected String name;
    protected String slug;
    protected double price;
    protected float discount;
    protected long quantity;
    protected String shortDescription;
    protected String description;
    protected Category category;
    protected Brand brand;

    protected String cpu;
    protected String gpu;
    protected String ram;
    protected String storage;
    protected String io;
    protected String screen;
    protected String keyboard;
    protected String audio;
    protected String sdCard;
    protected String lan;
    protected String wifi;
    protected String bluetooth;
    protected String webCam;
    protected String os;
    protected String battery;
    protected String weight;
    protected String color;
    protected String size;
    protected String cooling;
    protected String material;

    protected Instant createdAt;
    protected Instant updatedAt;

    public double getDiscountPrice() {
        return price - ((price * discount) / 100);
    }
}
