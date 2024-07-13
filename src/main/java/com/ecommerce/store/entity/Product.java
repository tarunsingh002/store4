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
public class Product implements Comparable<Product> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String brand;
    private String category;
    private String name;
    private String description;
    private String url;
    private String specification;
    private float price;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<WishList> wishLists = new ArrayList<>();

    @Override
    public int compareTo(Product o) {
        return productId - o.productId;
    }
}
