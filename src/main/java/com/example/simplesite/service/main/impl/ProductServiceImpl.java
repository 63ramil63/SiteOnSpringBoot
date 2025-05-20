package com.example.simplesite.service.main.impl;

import com.example.simplesite.model.main.Product;
import com.example.simplesite.repository.main.ProductRepository;
import com.example.simplesite.service.main.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long Id) {
        return productRepository.findById(Id);
    }

    @Override
    public List<Product> findAllByFilters(List<String> companyNames, List<String> types) {
        return productRepository.findAllByFilters(companyNames, types);
    }

    @Override
    public List<String> findDistinctTypes() {
        return productRepository.findDistinctTypes();
    }

    @Override
    public List<String> findDistinctCompanyNames() {
        return productRepository.findDistinctCompanyNames();
    }


    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
