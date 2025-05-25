package com.example.simplesite.repository.main.addon;

import com.example.simplesite.model.main.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterByCompanyName(List<String> companyNames) {
        return ((root, query, criteriaBuilder) -> {
            if (companyNames == null || companyNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("companyName").in(companyNames);
        });
    }

    public static Specification<Product> filterByType(List<String> types) {
        return ((root, query, criteriaBuilder) -> {
            if (types == null || types.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("type").in(types);
        });
    }
}
