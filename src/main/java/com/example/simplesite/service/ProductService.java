package com.example.simplesite.service;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();
    List<Order> findProductsByType(String type);
    List<Order> findProductByCompany(String companyName);
    void addProduct(Product product);
    void deleteProduct(Long id);
}
