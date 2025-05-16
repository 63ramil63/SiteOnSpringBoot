package com.example.simplesite.service.main;

import com.example.simplesite.model.main.ProductFeature;

import java.util.List;

public interface ProductFeatureService {
    ProductFeature findByParam(String key);
    void addFeature(String key, String value, Long productId);
    List<ProductFeature> findAllByProductId(String productId);
    void deleteFeature(String key);
}
