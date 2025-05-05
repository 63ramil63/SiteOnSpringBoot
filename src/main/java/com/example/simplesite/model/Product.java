package com.example.simplesite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Products")
public class Product {
    @Id
    private Long Id;
    private String name;
    private String src;
    private float price;
}
