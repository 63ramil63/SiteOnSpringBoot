package com.example.simplesite.service.main.impl;

import com.example.simplesite.model.main.Product;
import com.example.simplesite.model.main.ProductFeature;
import com.example.simplesite.repository.main.ProductFeatureRepository;
import com.example.simplesite.repository.main.ProductRepository;
import com.example.simplesite.service.main.ProductFeatureService;
import com.example.simplesite.service.main.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductFeatureServiceImpl implements ProductFeatureService {

    private ProductFeatureRepository repository;
    private ProductRepository productRepository;

    @Override
    public ProductFeature findByParam(String key) {
        return repository.findByParam(key);
    }

    @Override
    public void addFeature(String key, String value, Long productId) {
        ProductFeature productFeature = new ProductFeature();
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productFeature.setProduct(product.get());
            productFeature.setParam(key);
            productFeature.setValue(value);
            repository.save(productFeature);
        }
    }

    @Override
    public List<ProductFeature> findAllByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    @Override
    public void deleteFeature(String key) {

    }
}
