package com.example.simplesite.repository.main.addon;

import com.example.simplesite.model.main.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    //Specification<Product> — это интерфейс, который описывает условие поиска для JPA
    public static Specification<Product> filterByCompanyName(List<String> companyNames) {
        //Лямбда-выражение (root, query, criteriaBuilder) -> { ... }: ->
        //Это реализация метода toPredicate, который строит условие поиска.
        return ((root, query, criteriaBuilder) -> {
            if (companyNames == null || companyNames.isEmpty()) {
                //ничего не фильтровать
                return criteriaBuilder.conjunction();
            }
            //возвращает условие
            return root.get("companyName").in(companyNames);
        });
    }

    public static Specification<Product> filterByType(List<String> types) {
        //Лямбда-выражение (root, query, criteriaBuilder) -> { ... }: ->
        //Это реализация метода toPredicate, который строит условие поиска.
        return ((root, query, criteriaBuilder) -> {
            if (types == null || types.isEmpty()) {
                //ничего не фильтровать
                return criteriaBuilder.conjunction();
            }
            //возвращает условие
            return root.get("type").in(types);
        });
    }

    public static Specification<Product> filterByName(String name) {
        return (((root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), "%" + name.trim() + "%");
        }));
    }
}
