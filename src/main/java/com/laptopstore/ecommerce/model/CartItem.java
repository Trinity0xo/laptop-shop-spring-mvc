package com.laptopstore.ecommerce.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public CartItem(Cart cart, Product product, int quantity){
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
