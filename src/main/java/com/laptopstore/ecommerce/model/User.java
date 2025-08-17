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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    private List<Token> tokens = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @Column(unique = true)
    private String avatar;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @Column(unique = true, nullable = false)
    @NotNull
    private String email;

    private String password;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String phone;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public User(String firstName, String lastName, String email, String password, Role role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
