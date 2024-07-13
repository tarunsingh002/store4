package com.ecommerce.store.dto;

import com.ecommerce.store.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    private Product product;
    private int quantity;
}
