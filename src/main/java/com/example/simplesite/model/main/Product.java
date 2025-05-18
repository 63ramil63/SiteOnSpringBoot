package com.example.simplesite.model.main;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imgSrc;
    private int price;
    private String type;
    private String companyName;
    //cascade указывает, Все операции с объектом Product автоматически распространяются на связанные объекты ProductFeature
    //сли вы удалите характеристику из коллекции (product.getFeatures().remove(someFeature)), то эта характеристика будет автоматически удалена из базы данных при сохранении
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> features = new ArrayList<>();
}
