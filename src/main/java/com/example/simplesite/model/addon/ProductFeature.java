package com.example.simplesite.model.addon;

import com.example.simplesite.model.main.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_feature")
public class ProductFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String param;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
