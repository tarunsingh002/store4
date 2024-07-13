package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

//    Page<Product> findByNameOrBrandOrCategoryContaining(String searchTerm, String searchTerm1, String searchTerm2, Pageable p);

//    Page<Product> findByBrandIn(List<String> brands, Pageable p);
//
//    Page<Product> findByCategoryIn(List<String> categories, Pageable p);


}
