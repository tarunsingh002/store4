package com.ecommerce.store.controller;

import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.WishList;
import com.ecommerce.store.service.OrderService;
import com.ecommerce.store.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WishListService wishListService;

    @GetMapping("/api/v1/admin/getallorders")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/api/v1/admin/getallwishlisted")
    public List<WishList> getWishLists() {
        return wishListService.getAllWishLists();
    }

    @GetMapping("/api/v1/admin/getorder/{id}")
    public Order getOrder(@PathVariable int id) {
        return orderService.getOrder(id);
    }
}
