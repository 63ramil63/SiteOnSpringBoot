package com.example.simplesite.service.impl;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import com.example.simplesite.repository.ProductRepository;
import com.example.simplesite.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Order> findProductsByType(String type) {
        return productRepository.findAllByType(type);
    }

    @Override
    public List<Order> findProductByCompany(String companyName) {
        return productRepository.findAllByCompanyName(companyName);
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
