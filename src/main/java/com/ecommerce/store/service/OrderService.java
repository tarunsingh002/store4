package com.ecommerce.store.service;

import com.ecommerce.store.dto.OrderItemDto;
import com.ecommerce.store.entity.Order;
import com.ecommerce.store.entity.OrderItem;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.repository.OrderItemRepository;
import com.ecommerce.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(List<OrderItemDto> orderItemDtos, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setTimestamp(System.currentTimeMillis());

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItemDtos.forEach(i -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(i.getProduct());
            orderItem.setQuantity(i.getQuantity());
            orderItem = orderItemRepository.save(orderItem);
            orderItems.add(orderItem);


        });
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);
        return order;
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findAllByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(int id) {
        return orderRepository
                .findById(id)
                .orElse(null);
    }
}
