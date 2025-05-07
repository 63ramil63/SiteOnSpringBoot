package com.example.simplesite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    private Long Id;
    private String name;
    private String imgSrc;
    private float price;
    private String type;
    private String companyName;
}
