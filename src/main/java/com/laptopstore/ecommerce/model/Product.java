package com.laptopstore.ecommerce.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @Column(unique = true, nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private double price;

    @Column(nullable = false)
    @NotNull
    private float discount;

    @Column(nullable = false)
    @NotNull
    private long quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String shortDescription;

    @Column(unique = true, nullable = false)
    @NotNull
    private String slug;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(columnDefinition = "TEXT")
    private String cpu;

    @Column(columnDefinition = "TEXT")
    private String gpu;

    @Column(columnDefinition = "TEXT")
    private String ram;

    @Column(columnDefinition = "TEXT")
    private String storage;

    @Column(columnDefinition = "TEXT")
    private String io;

    @Column(columnDefinition = "TEXT")
    private String screen;

    @Column(columnDefinition = "TEXT")
    private String keyboard;

    @Column(columnDefinition = "TEXT")
    private String audio;

    @Column(columnDefinition = "TEXT")
    private String sdCard;

    @Column(columnDefinition = "TEXT")
    private String lan;

    @Column(columnDefinition = "TEXT")
    private String wifi;

    @Column(columnDefinition = "TEXT")
    private String bluetooth;

    @Column(columnDefinition = "TEXT")
    private String webCam;

    @Column(columnDefinition = "TEXT")
    private String os;

    @Column(columnDefinition = "TEXT")
    private String battery;

    @Column(columnDefinition = "TEXT")
    private String weight;

    @Column(columnDefinition = "TEXT")
    private String color;

    @Column(columnDefinition = "TEXT")
    private String size;

    @Column(columnDefinition = "TEXT")
    private String cooling;

    @Column(columnDefinition = "TEXT")
    private String material;

    public double getDiscountPrice(){
        return price - ((discount * price) / 100);
    }

    public Product(
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
            String material
    ) {
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
    }
}
