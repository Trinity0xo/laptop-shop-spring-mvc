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
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Product product;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    public OrderItem(Order order, Product product, double price, int quantity){
        this.order = order;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }
}
