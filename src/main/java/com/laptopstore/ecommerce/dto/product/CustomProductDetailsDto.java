package com.laptopstore.ecommerce.dto.product;

import com.laptopstore.ecommerce.dto.review.CustomReviewDto;
import com.laptopstore.ecommerce.model.Brand;
import com.laptopstore.ecommerce.model.Category;
import com.laptopstore.ecommerce.model.ProductImage;
import com.laptopstore.ecommerce.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CustomProductDetailsDto extends BaseProductDto {
    // product information
    private long id;
    private String slug;
    private long sold;
    private double discountPrice;

    // product images
    private List<ProductImage> productImages;

    // product image
    private ProductImage productImage;

    // review and rating information
    private long totalRatings;
    private double averageRating;
    private Map<Integer, Integer> ratingPercentages;
    private Map<Integer, Long> ratingCount;
    private List<Review> userReviews;
    private Review myReview;
    private boolean purchased;

    // related products
    private List<CustomProductDto> relatedProducts;

    // all reviews
    private List<CustomReviewDto> reviews;

    // ???
    public CustomProductDetailsDto(long id, String name, String slug, List<CustomReviewDto> reviews){
        this.id = id;
        this.name = name;
        this.reviews = reviews;
        this.slug = slug;
    }


    // product reviews
    public CustomProductDetailsDto(long id,
                                ProductImage productImage,
                                String name,
                                String slug,

                                Review myReview,
                                boolean purchased,
                                long totalRatings,
                                double averageRating,
                                Map<Integer, Integer> ratingPercentages,
                                Map<Integer, Long> ratingCount,
                                List<Review> userReviews) {
        this.id = id;
        this.productImage = productImage;
        this.name = name;
        this.slug = slug;
        this.myReview = myReview;
        this.purchased = purchased;
        this.totalRatings = totalRatings;
        this.averageRating = averageRating;
        this.ratingPercentages = ratingPercentages != null ? ratingPercentages : new HashMap<>();
        this.ratingCount = ratingCount != null ? ratingCount : new HashMap<>();
        this.userReviews = userReviews != null ? userReviews : new ArrayList<>();
    }

    public CustomProductDetailsDto(long id,
                                ProductImage productImage,
                                String name,
                                String slug,
                                double price,
                                float discount,
                                double discountPrice,

                                Review myReview,
                                boolean purchased,
                                long totalRatings,
                                double averageRating,
                                Map<Integer, Integer> ratingPercentages,
                                Map<Integer, Long> ratingCount,
                                List<Review> userReviews) {
        this.id = id;
        this.productImage = productImage;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;

        this.myReview = myReview;
        this.purchased = purchased;
        this.totalRatings = totalRatings;
        this.averageRating = averageRating;
        this.ratingPercentages = ratingPercentages != null ? ratingPercentages : new HashMap<>();
        this.ratingCount = ratingCount != null ? ratingCount : new HashMap<>();
        this.userReviews = userReviews != null ? userReviews : new ArrayList<>();
    }

    public CustomProductDetailsDto(long id,
                                ProductImage productImage,
                                String name,
                                String slug,
                                double price,
                                float discount,
                                double discountPrice,

                                long totalRatings,
                                double averageRating,
                                Map<Integer, Integer> ratingPercentages,
                                Map<Integer, Long> ratingCount,
                                List<Review> userReviews) {
        this.id = id;
        this.productImage = productImage;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;

        this.totalRatings = totalRatings;
        this.averageRating = averageRating;
        this.ratingPercentages = ratingPercentages != null ? ratingPercentages : new HashMap<>();
        this.ratingCount = ratingCount != null ? ratingCount : new HashMap<>();
        this.userReviews = userReviews != null ? userReviews : new ArrayList<>();
    }

    // product details
    public CustomProductDetailsDto(long id,
                                List<ProductImage> productImages,
                                String name,
                                double price,
                                float discount,
                                double discountPrice,
                                long quantity,
                                String shortDescription,
                                String description,
                                Category category,
                                Brand brand,
                                String slug,

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

                                long totalRatings,
                                double averageRating,
                                Map<Integer, Integer> ratingPercentages,
                                Map<Integer, Long> ratingCount,
                                List<Review> userReviews,
                                List<CustomProductDto> relatedProducts

    ){
        this.id = id;
        this.productImages = productImages;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.slug = slug;

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

        this.relatedProducts = relatedProducts;
        this.totalRatings = totalRatings;
        this.averageRating = averageRating;
        this.ratingPercentages = ratingPercentages;
        this.ratingCount = ratingCount;
        this.userReviews = userReviews;
    }

    public CustomProductDetailsDto(long id,
                                List<ProductImage> productImages,
                                String name,
                                Double price,
                                float discount,
                                double discountPrice,
                                long quantity,
                                String shortDescription,
                                String description,
                                Category category,
                                Brand brand,
                                String slug,

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

                                long totalRatings,
                                double averageRating,
                                Map<Integer, Integer> ratingPercentages,
                                Map<Integer, Long> ratingCount,
                                List<Review> userReviews,
                                Review myReview,
                                boolean purchased,
                                   List<CustomProductDto> relatedProducts
    ){
        this.id = id;
        this.productImages = productImages;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.slug = slug;

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

        this.totalRatings = totalRatings;
        this.averageRating = averageRating;
        this.ratingPercentages = ratingPercentages;
        this.ratingCount = ratingCount;
        this.userReviews = userReviews;
        this.myReview = myReview;
        this.purchased = purchased;
        this.relatedProducts = relatedProducts;
    }

    public CustomProductDetailsDto(long id,
                                List<ProductImage> productImages,
                                String name,
                                double price,
                                float discount,
                                long quantity,
                                String shortDescription,
                                String description,
                                Category category,
                                Brand brand,
                                String slug,

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
                                Long sold,
                                Instant createdAt,
                                Instant updatedAt
    ){
        this.id = id;
        this.productImages = productImages;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.slug = slug;

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
        this.sold = sold;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
