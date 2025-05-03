package com.laptopstore.ecommerce.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.laptopstore.ecommerce.util.constant.OrderStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    List<OrderDetails> orderDetails = new ArrayList<>();

    private double totalPrice = 0.0;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;
    private String receiverAddress;
    private String receiverPhone;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String receiverNote;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod = PaymentMethodEnum.COD;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus = OrderStatusEnum.PENDING;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String cancelledReason;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
