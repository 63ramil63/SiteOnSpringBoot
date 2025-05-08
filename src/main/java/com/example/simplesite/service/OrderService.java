package com.example.simplesite.service;

import com.example.simplesite.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllProducts();
    void addProduct(Order order);
    void deleteProduct(Long id);
}