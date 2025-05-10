package com.example.simplesite.service;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByEmail(String email);
    void addOrder(String userEmail, Long productId);
    void deleteOrder(Long id);
}