package com.laptopstore.ecommerce.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.laptopstore.ecommerce.util.constant.PaymentMethodEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Order {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    @NotNull
    private double totalPrice;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    private String address;

    @Column(nullable = false)
    @NotNull
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private PaymentMethodEnum paymentMethod = PaymentMethodEnum.COD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private OrderStatusEnum status = OrderStatusEnum.PENDING;

    @Column(columnDefinition = "TEXT")
    private String cancelledReason;

    private Instant cancelledAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    private Instant deliveredAt;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Order(User user, String firstName, String lastName,
                String email, String address, String phone, String note){
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.note = note;
    }
}
