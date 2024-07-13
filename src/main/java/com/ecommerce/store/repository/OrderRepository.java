package com.ecommerce.store.repository;

import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findAllByUser(User user);
}
