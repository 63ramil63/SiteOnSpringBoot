package com.example.simplesite.service;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> findById(Long Id);
    List<Order> findProductsByType(String type);
    List<Order> findProductByCompany(String companyName);
    @Transactional
    void addProduct(Product product);
    void deleteProduct(Long id);
}
