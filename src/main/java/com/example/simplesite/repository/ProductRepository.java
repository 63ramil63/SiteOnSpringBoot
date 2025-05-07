package com.example.simplesite.repository;

import com.example.simplesite.model.Order;
import com.example.simplesite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Order> findAllByType(String type);
    List<Order> findAllByCompanyName(String companyName);
}
