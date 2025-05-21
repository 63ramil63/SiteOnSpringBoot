package com.example.simplesite.service.main;

import com.example.simplesite.model.addon.ProductFeatureTemplate;

import java.util.List;

public interface ProductFeatureTemplateService {
    ProductFeatureTemplate findProductFeatureTemplateByType(String type);
    void save(ProductFeatureTemplate productFeatureTemplate);
    List<String> findParamsByProductType(String type);
    List<String> findDistinctTypes();
    void deleteProductFeatureTemplateByType(String type);
}
