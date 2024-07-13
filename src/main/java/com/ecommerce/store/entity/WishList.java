package com.ecommerce.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wishlist4")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToMany
    @JoinTable(name = "wishlist4_product", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    Set<Product> products = new HashSet<>();
}
