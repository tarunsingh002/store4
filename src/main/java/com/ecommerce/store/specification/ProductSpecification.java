package com.ecommerce.store.specification;

import com.ecommerce.store.entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> brandContains(String brand) {
        return (root, query, builder) -> {
            Expression<String> brandLowerCase = builder.lower(root.get("brand"));
            return builder.like(brandLowerCase, "%" + brand.toLowerCase() + "%");
        };
    }

    public static Specification<Product> brandIn(List<String> brands) {
        return (root, query, builder) -> builder.lower(root.get("brand")).in(brands);
    }

    public static Specification<Product> categoryContains(String category) {
        return (root, query, builder) -> {
            Expression<String> categoryLowerCase = builder.lower(root.get("category"));
            return builder.like(categoryLowerCase, "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, builder) -> {
            Expression<String> nameLowerCase = builder.lower(root.get("name"));
            return builder.like(nameLowerCase, "%" + name.toLowerCase() + "%");
        };
    }
}
