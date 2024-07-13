package com.ecommerce.store.controller;

import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.service.JWTService;
import com.ecommerce.store.service.UserService;
import com.ecommerce.store.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class WishListController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;


    @Autowired
    private WishListService wishListService;

    @GetMapping("/api/v1/user/getwishlist")
    public Set<Product> getWishList(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return wishListService.getWishList(user);
        } else return null;
    }

    @PostMapping("/api/v1/user/addproduct")
    public String addProduct(@RequestBody Product product, @RequestHeader(
            "Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return wishListService.addProduct(user, product);
        } else return null;
    }

    @DeleteMapping("/api/v1/user/deleteproduct/{id}")
    public String removeProduct(@PathVariable int id, @RequestHeader(
            "Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return wishListService.removeProduct(id, user);
        } else return null;
    }

    @GetMapping("/api/v1/user/iswishlisted/{id}")
    public String isProductWishListed(@PathVariable int id, @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        if (userService.userExists(userEmail)) {
            User user = (User) userService.loadUserByUsername(userEmail);
            return wishListService.isProductWishListed(id, user);
        } else return null;
    }

    @GetMapping("/api/v1/admin/productinwishlists/{id}")
    public long numberOfTimesWishListed(@PathVariable int id) {
        return wishListService.numberOfTimesWishListed(id);
    }
}
