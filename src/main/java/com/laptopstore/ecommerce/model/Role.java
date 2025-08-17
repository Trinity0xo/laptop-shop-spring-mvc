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

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

    @Column(unique = true, nullable = false)
    @NotNull
    private String name;

    @Column(unique = true, nullable = false)
    @NotNull
    private String slug;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Role(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }
}
