package com.laptopstore.ecommerce.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "brands")
@NoArgsConstructor
public class Brand {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @Column(unique = true, nullable = false)
    @NotNull
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(unique = true, nullable = false)
    @NotNull
    private String slug;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Brand(String name, String description, String slug){
        this.name = name;
        this.description = description;
        this.slug = slug;
    }
}
