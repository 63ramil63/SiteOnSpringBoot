package com.example.simplesite.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String imgSrc;
    private int price;
    private String type;
    private String companyName;
}
