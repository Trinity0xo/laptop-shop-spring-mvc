package com.laptopstore.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(unique = true, nullable = false)
    @NotNull
    private String imageName;

    @Column(nullable = false)
    @NotNull
    private boolean isMain;

    public ProductImage(Product product, String imageName, boolean isMain){
        this.product = product;
        this.imageName = imageName;
        this.isMain = isMain;
    }
}
