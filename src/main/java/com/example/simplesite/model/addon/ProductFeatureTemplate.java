package com.example.simplesite.model.addon;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ProductFeatureTemplate {
    @Id
    private String type;
    @ElementCollection
    private List<String> params;
}
