package com.example.simplesite.repository.main;

import com.example.simplesite.model.main.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    ProductFeature findByParam(String param);
    List<ProductFeature> findByProductId(Long productId);
}
