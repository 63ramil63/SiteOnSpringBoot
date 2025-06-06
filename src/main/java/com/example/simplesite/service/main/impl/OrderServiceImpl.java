package com.example.simplesite.service.main.impl;

import com.example.simplesite.model.main.Order;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.repository.main.OrderRepository;
import com.example.simplesite.service.main.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private ProductServiceImpl productService;

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }

    @Override
    public void addOrder(String userEmail, Long productId) {
        Order order = new Order();
        order.setUserEmail(userEmail);
        Optional<Product> optionalProduct = productService.findById(productId);
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            order.setProduct(product);
            orderRepository.save(order);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
