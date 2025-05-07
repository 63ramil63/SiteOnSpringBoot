package com.example.simplesite.service.impl;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import com.example.simplesite.repository.OrderRepository;
import com.example.simplesite.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllProducts() {
        return orderRepository.findAll();
    }

    @Override
    public void addProduct(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteProduct(Long id) {
        orderRepository.deleteById(id);
    }
}
