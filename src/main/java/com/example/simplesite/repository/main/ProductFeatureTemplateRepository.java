package com.example.simplesite.repository.main;

import com.example.simplesite.model.addon.ProductFeatureTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductFeatureTemplateRepository extends JpaRepository<ProductFeatureTemplate, String> {
    @Query("SELECT p FROM ProductFeatureTemplate p WHERE p.type = ?1")
    ProductFeatureTemplate findByType(String type);

    @Query("SELECT p.params FROM ProductFeatureTemplate p WHERE p.type = ?1")
    List<String> findParamsByType(String type);

    @Query("SELECT DISTINCT p.type FROM ProductFeatureTemplate p")
    List<String> findDistinctTypes();
}
