package com.example.simplesite.service.main;

import com.example.simplesite.model.main.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByEmail(String email);
    void addOrder(String userEmail, Long productId);
    void deleteOrder(Long id);
}