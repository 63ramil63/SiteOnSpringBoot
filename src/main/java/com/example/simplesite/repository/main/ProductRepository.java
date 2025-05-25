package com.example.simplesite.repository.main;

import com.example.simplesite.model.main.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT DISTINCT p.type FROM Product p")
    List<String> findDistinctTypes();

    @Query("SELECT DISTINCT p.companyName FROM Product p")
    List<String> findDistinctCompanyNames();
}
