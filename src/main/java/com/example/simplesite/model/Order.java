package com.example.simplesite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    private Long id;
    @ManyToOne
    private Product product;
    private String user;
}
