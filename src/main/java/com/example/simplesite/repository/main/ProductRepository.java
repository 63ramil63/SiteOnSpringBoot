package com.example.simplesite.repository.main;

import com.example.simplesite.model.main.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Здесь @Param("companyName") связывает аргумент метода с :companyName в запросе
    @Query("SELECT p FROM Product p WHERE (:companyNames IS NULL OR p.companyName in :companyNames)" +
            "AND (:types IS NULL OR p.type in :types)")
    List<Product> findAllByFilters(@Param("companyNames") List<String> companyNames, @Param("types") List<String> types);

    @Query("SELECT DISTINCT p.type FROM Product p")
    List<String> findDistinctTypes();

    @Query("SELECT DISTINCT p.companyName FROM Product p")
    List<String> findDistinctCompanyNames();
}
