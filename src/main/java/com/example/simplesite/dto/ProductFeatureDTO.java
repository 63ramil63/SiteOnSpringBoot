package com.example.simplesite.dto;

import com.example.simplesite.model.addon.ProductFeature;
import lombok.Data;

import java.util.List;

@Data
public class ProductFeatureDTO {
    private Long id;
    private List<ProductFeature> features;
}
