package com.laptopstore.ecommerce.model;

import com.laptopstore.ecommerce.util.constant.TokenTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token {
    private static final int EXPIRATION_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(unique = true, nullable = false)
    @NotNull
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TokenTypeEnum type;

    @Column(nullable = false)
    @NotNull
    private Instant expiryDate;

    @PrePersist
    private void prePersist() {
        this.expiryDate = Instant.now().plus(Duration.ofHours(EXPIRATION_HOURS));
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public Token(User user, String value, TokenTypeEnum type){
        this.user = user;
        this.value = value;
        this.type = type;
    }
}
