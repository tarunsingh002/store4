package com.ecommerce.store.controller;

import com.ecommerce.store.dto.FilterDto;
import com.ecommerce.store.dto.ProductResponse;
import com.ecommerce.store.entity.Product;
import com.ecommerce.store.service.ProductService;
import com.ecommerce.store.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private WishListService wishListService;

    @PostMapping("/api/v1/admin/addproduct")
    public Product addProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    @PutMapping("/api/v1/admin/updateproduct/{id}")
    public Product updateProduct(@RequestBody Product product,
                                 @PathVariable int id) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/api/v1/admin/deleteproduct/{id}")
    public String deleteProduct(@PathVariable int id) {
        if (wishListService.numberOfTimesWishListed(id) == 0)
            return service.deleteProductById(id);
        else {
            wishListService.removeFromAllWishLists(id);
            return service.deleteProductById(id);
        }
    }

    @GetMapping("/api/v1/all/getproducts")
    public ProductResponse getProducts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
                                       @RequestParam(value = "direction", defaultValue = "asc", required = false) String direction) {
        return service.getProducts(pageSize, pageNumber, sortBy, direction);
    }


    @PostMapping("/api/v1/all/filterproducts")
    public ProductResponse filterProducts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
                                          @RequestParam(value = "direction", defaultValue = "asc", required = false) String direction,
                                          @RequestParam(value = "searchTerm", defaultValue = "", required = false) String searchTerm,
                                          @RequestBody FilterDto filterDto
    ) {
        return service.filterProducts(pageSize, pageNumber, searchTerm, filterDto.getBrands(), filterDto.getCategories(), sortBy, direction);
    }


    @GetMapping("/api/v1/all/getproduct/{id}")
    public Product getProduct(@PathVariable int id) {
        return service.getProductById(id);
    }
}
