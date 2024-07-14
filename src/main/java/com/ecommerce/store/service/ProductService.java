package com.ecommerce.store.service;

import com.ecommerce.store.dto.ProductResponse;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public ProductResponse getProducts(int pageSize, int pageNumber, String sortBy, String direction) {

        if (pageNumber != -1) {
            Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable p = PageRequest.of(pageNumber, pageSize, sort);

            Page<Product> pageProduct = repository.findAll(p);

            return ProductResponse.builder().products(pageProduct.getContent())
                    .lastPage(pageProduct.isLast()).pageNumber(pageProduct.getNumber())
                    .totalElements(pageProduct.getTotalElements()).pageSize(pageProduct.getSize())
                    .totalPages(pageProduct.getTotalPages()).build();
        } else {

            List<Product> products = repository.findAll();
            return ProductResponse.builder().products(products)
                    .lastPage(true).pageNumber(-1)
                    .totalElements(products.size()).pageSize(products.size())
                    .totalPages(1).build();
        }

    }

    public ProductResponse filterProducts(int pageSize, int pageNumber, String searchTerm, List<String> brands, List<String> categories, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Specification<Product> brandsSpecification = null;
//        for (String brand : brands) {
//            Specification<Product> brandSpecification = ProductSpecification.brandContains(brand);
//            if (brandsSpecification == null) {
//                brandsSpecification = brandSpecification;
//            } else {
//                brandsSpecification = brandsSpecification.or(brandSpecification);
//            }
//        }

        if (brands.size() > 0) brandsSpecification = ProductSpecification.brandIn(brands);

        Specification<Product> categoriesSpecification = null;

        for (String category : categories) {
            Specification<Product> categorySpecification = ProductSpecification.categoryContains(category);
            if (categoriesSpecification == null) {
                categoriesSpecification = categorySpecification;
            } else {
                categoriesSpecification = categoriesSpecification.or(categorySpecification);
            }
        }


        Specification<Product> searchSpecification = null;

        if (searchTerm.trim().length() != 0) {
            Specification<Product> searchBrandSpecification = ProductSpecification.brandContains(searchTerm);
            Specification<Product> searchCategorySpecification = ProductSpecification.categoryContains(searchTerm);
            Specification<Product> searchNameSpecification = ProductSpecification.nameContains(searchTerm);
            searchSpecification = searchBrandSpecification
                    .or(searchCategorySpecification)
                    .or(searchNameSpecification);
        }


        Page<Product> pageProduct = repository.findAll(p);

        Specification<Product> combinedSpecification = null;

        if (brandsSpecification != null && categoriesSpecification != null && searchSpecification != null)
            combinedSpecification = brandsSpecification.and(categoriesSpecification).and(searchSpecification);
        else if (brandsSpecification == null && categoriesSpecification != null && searchSpecification != null)
            combinedSpecification = categoriesSpecification.and(searchSpecification);
        else if (brandsSpecification == null && categoriesSpecification == null && searchSpecification != null)
            combinedSpecification = searchSpecification;
        else if (brandsSpecification == null && categoriesSpecification != null && searchSpecification == null)
            combinedSpecification = categoriesSpecification;
        else if (brandsSpecification != null && categoriesSpecification == null && searchSpecification == null)
            combinedSpecification = brandsSpecification;
        else if (brandsSpecification != null && categoriesSpecification == null && searchSpecification != null)
            combinedSpecification = brandsSpecification.and(searchSpecification);
        else if (brandsSpecification != null && categoriesSpecification != null && searchSpecification == null)
            combinedSpecification = brandsSpecification.and(categoriesSpecification);


        pageProduct = combinedSpecification != null ? repository.findAll(combinedSpecification, p) : pageProduct;

        return ProductResponse.builder().products(pageProduct.getContent()).totalPages(pageProduct.getTotalPages())
                .totalElements(pageProduct.getTotalElements()).lastPage(pageProduct.isLast())
                .pageSize(pageProduct.getSize()).pageNumber(pageProduct.getNumber()).build();

    }

    public Product getProductById(int id) {
        return repository
                .findById(id)
                .orElse(null);
    }

    public String deleteProductById(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Product Deleted";
        } else return "Product Not Found";
    }

    public Product updateProduct(int id, Product product) {
        Product existingProduct = repository
                .findById(id)
                .orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setSpecification(product.getSpecification());
            existingProduct.setUrl(product.getUrl());
            existingProduct.setPrice(product.getPrice());
            return repository.save(existingProduct);
        } else return null;

    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}
