package com.example.simplesite.dto;

import lombok.Data;

@Data
public class ProductDefaultInfoDTO {
    private Long id;
    private String name;
    private String imgSrc;
    private int price;
    private String type;
    private String companyName;
}
