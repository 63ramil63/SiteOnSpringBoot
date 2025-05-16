package com.example.simplesite.service.main;

import com.example.simplesite.model.main.Order;
import com.example.simplesite.model.main.Product;
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
