package com.example.simplesite.service.main.impl;

import com.example.simplesite.model.addon.ProductFeatureTemplate;
import com.example.simplesite.repository.main.ProductFeatureTemplateRepository;
import com.example.simplesite.service.main.ProductFeatureTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductFeatureTemplateServiceImpl implements ProductFeatureTemplateService {

    private ProductFeatureTemplateRepository repository;

    @Override
    public ProductFeatureTemplate findProductFeatureTemplateByType(String type) {
        return repository.findByType(type);
    }

    @Override
    public void save(ProductFeatureTemplate productFeatureTemplate) {
        repository.save(productFeatureTemplate);
    }

    @Override
    public List<String> findParamsByProductType(String type) {
        return repository.findParamsByType(type);
    }

    @Override
    public List<String> findDistinctTypes() {
        return repository.findDistinctTypes();
    }

    @Override
    public void deleteProductFeatureTemplateByType(String type) {
        repository.deleteById(type);
    }
}
