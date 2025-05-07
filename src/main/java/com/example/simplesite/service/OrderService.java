package com.example.simplesite.service;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    List<Order> getAllProducts();
    void addProduct(Order order);
    void deleteProduct(Long id);
}