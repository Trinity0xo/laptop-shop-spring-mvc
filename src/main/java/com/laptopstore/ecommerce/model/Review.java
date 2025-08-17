package com.laptopstore.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Review {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String message;

    @Column(nullable = false)
    @NotNull
    private int rating;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Review(User user, Product product, String message, int rating){
        this.user = user;
        this.product = product;
        this.message = message;
        this.rating = rating;
    }
}
