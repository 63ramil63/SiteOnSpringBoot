package com.example.simplesite.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductFeatureTemplateDTO {
    String type;
    List<String> params;
}
